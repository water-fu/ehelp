package com.wisdom.service.service.sys.impl;

import com.wisdom.common.config.SystemSetting;
import com.wisdom.common.entity.SessionDetail;
import com.wisdom.dao.entity.*;
import com.wisdom.dao.mapper.AccountMapper;
import com.wisdom.dao.mapper.WeChatLoginMapper;
import com.wisdom.mapp.core.Application;
import com.wisdom.web.common.constants.CommonConstant;
import com.wisdom.web.common.constants.SysParamDetailConstant;
import com.wisdom.common.exception.ApplicationException;
import com.wisdom.common.util.DateUtil;
import com.wisdom.common.util.StringUtil;
import com.wisdom.dao.mapper.IdentifyCodeMapper;
import com.wisdom.service.service.sys.IIdentifyCodeService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 验证码
 * Created by fusj on 16/3/16.
 */
@Service
@Transactional
public class IdentifyCodeServiceImpl implements IIdentifyCodeService {

    @Autowired
    private IdentifyCodeMapper identifyCodeMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private WeChatLoginMapper weChatLoginMapper;

    @Autowired
    private SystemSetting systemSetting;

    /**
     * 发送验证码
     * @param phoneNo
     * @param type
     */
    @Override
    public String sendIdentifyCode(String phoneNo, String type, String ip, SessionDetail sessionDetail) {

        // 是注册的话，发送验证码先验证该手机号是否已经注册
        if(SysParamDetailConstant.IDENTIFY_TYPE_REGISTER.equals(type)) {
            AccountExample accountExample = new AccountExample();
            accountExample.createCriteria().andPhoneNoEqualTo(phoneNo);

            List<Account> accountList = accountMapper.selectByExample(accountExample);
            if(CollectionUtils.isNotEmpty(accountList)) {
                throw new ApplicationException("手机号码已被注册");
            }
        }
        // 忘记密码,校验手机号码是否存在
        else if(SysParamDetailConstant.IDENTIFY_TYPE_FOTGET.equals(type)) {
            AccountExample accountExample = new AccountExample();
            accountExample.createCriteria().andPhoneNoEqualTo(phoneNo);

            List<Account> accountList = accountMapper.selectByExample(accountExample);
            if(CollectionUtils.isEmpty(accountList)) {
                throw new ApplicationException("手机号码不存在");
            }
        }
        // 账号绑定
        else if(SysParamDetailConstant.IDENTIFY_TYPE_BIND.equals(type)) {
            AccountExample accountExample = new AccountExample();
            accountExample.createCriteria().andPhoneNoEqualTo(phoneNo);

            List<Account> accountList = accountMapper.selectByExample(accountExample);

            // 不为空
            if(CollectionUtils.isNotEmpty(accountList)) {
                Account account = accountList.get(0);

                // 如果账号类型，与登陆用户账号类型不一致，则提示手机号码已经注册
                if(!sessionDetail.getType().equals(account.getType())) {
                    throw new ApplicationException("手机号码已被注册");
                }

                // 数据来源，微信、微博、QQ，不同的来源需要不同的表中进行校验，判断是否已经绑定
                String from = sessionDetail.getFrom();

                switch (from) {
                    // 微信
                    case SysParamDetailConstant.LOGIN_FROM_WECHAT:
                        WeChatLoginExample example = new WeChatLoginExample();
                        example.createCriteria().andAccountIdEqualTo(account.getId());

                        List<WeChatLogin> weChatLoginList = weChatLoginMapper.selectByExample(example);
                        if(CollectionUtils.isNotEmpty(weChatLoginList)) {
                            throw new ApplicationException("手机号码已经被绑定");
                        }
                        break;
                }
            }

        }
        else {
            throw new ApplicationException("Type参数不正确");
        }

        String code;

        IdentifyCodeExample example = new IdentifyCodeExample();
        example.createCriteria().andPhoneNoEqualTo(phoneNo)
                .andTypeEqualTo(type);

        List<IdentifyCode> list = identifyCodeMapper.selectByExample(example);
        // 数据库中不存在
        if(CollectionUtils.isEmpty(list)) {
            IdentifyCode identifyCode = new IdentifyCode();

            identifyCode.setType(type);
            identifyCode.setPhoneNo(phoneNo);
            identifyCode.setIpCode(ip);

            // 生产环境发送验证码
            if(systemSetting.getProductionMode()) {
                identifyCode.setCode(StringUtil.genIdentifyCode());
            } else {
                identifyCode.setCode(CommonConstant.DEFAULT_IDENTIFY_CODE);
            }

            identifyCode.setTime(1);
            identifyCode.setCreateTime(DateUtil.getTimestamp());
            // 验证码过期时间15分钟
            identifyCode.setInvalidTime(DateUtil.getTimestampByMinutes(CommonConstant.IDENTIFY_CODE_INVALID));
            identifyCode.setIsUsed(SysParamDetailConstant.IS_USED_FALSE);

            identifyCodeMapper.insertSelective(identifyCode);

            // 验证码
            code = identifyCode.getCode();
        }
        // 已经存在
        else {
            IdentifyCode identifyCode = list.get(0);

            // 已使用或已过期，重新生成验证码，并重新设定时间
            if(SysParamDetailConstant.IS_USED_TRUE.equals(identifyCode.getIsUsed()) ||
                    !DateUtil.isValid(identifyCode.getInvalidTime())) {

                identifyCode.setIpCode(ip);

                // 生产环境发送验证码
                if(systemSetting.getProductionMode()) {
                    identifyCode.setCode(StringUtil.genIdentifyCode());
                } else {
                    identifyCode.setCode(CommonConstant.DEFAULT_IDENTIFY_CODE);
                }

                identifyCode.setTime(1);
                identifyCode.setCreateTime(DateUtil.getTimestamp());
                // 验证码过期时间15分钟
                identifyCode.setInvalidTime(DateUtil.getTimestampByMinutes(CommonConstant.IDENTIFY_CODE_INVALID));
                identifyCode.setIsUsed(SysParamDetailConstant.IS_USED_FALSE);
                identifyCode.setUsedTime(null);

                identifyCodeMapper.updateByPrimaryKey(identifyCode);

                code = identifyCode.getCode();
            }
            // 未过期，再发送一遍相同的验证码，连续发送次数（time）+1，如果time>=设定次数，不发送密码，提示
            else {
                if(identifyCode.getTime() >= CommonConstant.SEND_IDENTIFY_TIMES) {
                    throw new ApplicationException("该手机号码" + CommonConstant.IDENTIFY_CODE_INVALID + "分钟内发送次数过多,请稍后再发");
                }

                identifyCode.setIpCode(ip);
                identifyCode.setTime(identifyCode.getTime() + 1);
                identifyCodeMapper.updateByPrimaryKeySelective(identifyCode);

                code = identifyCode.getCode();
            }
        }

        return code;
    }

    /**
     * 校验验证码是否正确
     * @param phoneNo
     * @param code
     * @param type
     * @return
     */
    @Override
    public Boolean validIdentifyCode(String phoneNo, String code, String type) {
        IdentifyCodeExample example = new IdentifyCodeExample();
        example.createCriteria().andPhoneNoEqualTo(phoneNo)
                .andTypeEqualTo(type);

        List<IdentifyCode> list = identifyCodeMapper.selectByExample(example);

        if(CollectionUtils.isEmpty(list)) {
            return false;
        }

        IdentifyCode identifyCode = list.get(0);

        // 已过期或已使用
        if(SysParamDetailConstant.IS_USED_TRUE.equals(identifyCode.getIsUsed()) ||
                !DateUtil.isValid(identifyCode.getInvalidTime())) {
            return false;
        }

        // 验证码是否一致
        if(!identifyCode.getCode().equals(code)) {
            return false;
        }

        // 修改为已使用，并设定使用时间
        identifyCode.setIsUsed(SysParamDetailConstant.IS_USED_TRUE);
        identifyCode.setUsedTime(DateUtil.getTimestamp());

        identifyCodeMapper.updateByPrimaryKeySelective(identifyCode);

        return true;
    }
}
