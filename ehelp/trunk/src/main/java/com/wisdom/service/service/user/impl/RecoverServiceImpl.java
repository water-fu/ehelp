package com.wisdom.service.service.user.impl;

import com.wisdom.common.entity.SessionDetail;
import com.wisdom.common.exception.ApplicationException;
import com.wisdom.common.util.DateUtil;
import com.wisdom.common.util.Pinyin4jUtil;
import com.wisdom.dao.entity.*;
import com.wisdom.dao.mapper.AccountMapper;
import com.wisdom.dao.mapper.RecoverMapper;
import com.wisdom.service.service.user.IRecoverService;
import com.wisdom.service.service.vfs.IFileService;
import com.wisdom.web.common.constants.SysParamDetailConstant;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 康复师
 * Created by fusj on 16/4/9.
 */
@Service
@Transactional
public class RecoverServiceImpl implements IRecoverService {

    @Autowired
    private IFileService fileService;

    @Autowired
    private RecoverMapper recoverMapper;

    @Autowired
    private AccountMapper accountMapper;

    /**
     * 患者认证(微信浏览器)
     * @param recover
     * @param headImg
     * @param bodyImg
     * @param sessionDetail
     */
    @Override
    public void identification(Recover recover, byte[] headImg, byte[] bodyImg, SessionDetail sessionDetail) {
        try {
            String headFileId = fileService.uploadFile(fileService.getServerConfig(), headImg, "jpg", sessionDetail.getAccountId() + "_" + System.currentTimeMillis() + "_" + Math.random(), sessionDetail.getAccountId(), false, true);
            String bodyFileId = fileService.uploadFile(fileService.getServerConfig(), bodyImg, "jpg", sessionDetail.getAccountId() + "_" + System.currentTimeMillis() + "_" + Math.random(), sessionDetail.getAccountId(), false, true);

            insertIdentification(recover, headFileId, bodyFileId, sessionDetail);
        } catch (Exception ex) {
            throw new ApplicationException("文件上传失败", ex);
        }
    }

    /**
     * 患者认证(非微信浏览器)
     * @param recover
     * @param headFileId
     * @param bodyFileId
     * @param sessionDetail
     */
    @Override
    public void identification(Recover recover, String headFileId, String bodyFileId, SessionDetail sessionDetail) {
        try {
            // 把临时文件变成正式文件
            fileService.tranFile(headFileId, sessionDetail.getAccountId(), fileService.getServerConfig());
            fileService.tranFile(bodyFileId, sessionDetail.getAccountId(), fileService.getServerConfig());
        } catch (Exception ex) {
            throw new ApplicationException("文件上传失败", ex);
        }

        insertIdentification(recover, headFileId, bodyFileId, sessionDetail);
    }

    /**
     * 康复师个人信息
     * @param recover
     * @param sessionDetail
     */
    @Override
    public void personalInfo(Recover recover, SessionDetail sessionDetail) {
        RecoverExample example = new RecoverExample();
        example.createCriteria().andAccountIdEqualTo(sessionDetail.getAccountId());

        recoverMapper.updateByExampleSelective(recover, example);
    }

    /**
     * 插入认证信息
     * @param recover
     * @param headFileId
     * @param bodyFileId
     * @param sessionDetail
     */
    private void insertIdentification(Recover recover, String headFileId, String bodyFileId, SessionDetail sessionDetail) {
        recover.setAccountId(sessionDetail.getAccountId());
        recover.setSimplePinyin(Pinyin4jUtil.translate(recover.getName(), Pinyin4jUtil.RET_PINYIN_TYPE_HEADCHAR));
        recover.setIdPath(headFileId);
        recover.setBodyPath(bodyFileId);
        recover.setIsDel(SysParamDetailConstant.IS_DEL_FALSE);
        recover.setCreateDate(DateUtil.getTimestamp());
        recover.setStatus(SysParamDetailConstant.RECOVER_STATUS_ADD); // 新增

        // 判断是否已经存在
        RecoverExample example = new RecoverExample();
        example.createCriteria().andAccountIdEqualTo(sessionDetail.getAccountId());

        List<Recover> list = recoverMapper.selectByExample(example);

        if(CollectionUtils.isEmpty(list)) {
            recoverMapper.insertSelective(recover);
        } else {
            recover.setId(list.get(0).getId());

            recover.setUpdateDate(DateUtil.getTimestamp());
            recover.setUpdateUser(sessionDetail.getAccountId());

            recoverMapper.updateByPrimaryKeySelective(recover);
        }

        // 账户设置为认证确认
        Account account = new Account();
        account.setId(sessionDetail.getAccountId());
        account.setStatus(SysParamDetailConstant.ACCOUNT_STATUS_AUTHEN);

        accountMapper.updateByPrimaryKeySelective(account);
    }
}
