package com.wisdom.service.service.user.impl;

import com.wisdom.common.constants.CommonConstant;
import com.wisdom.common.constants.SysParamDetailConstant;
import com.wisdom.common.encrypt.EncryptFactory;
import com.wisdom.common.entity.PageInfo;
import com.wisdom.common.exception.ApplicationException;
import com.wisdom.common.util.DateUtil;
import com.wisdom.common.util.StringUtil;
import com.wisdom.dao.entity.Account;
import com.wisdom.dao.entity.AccountExample;
import com.wisdom.dao.mapper.AccountMapper;
import com.wisdom.service.service.user.IAccountService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 账户管理
 * Created by fusj on 16/3/14.
 */
@Service
@Transactional
public class AccountServiceImpl implements IAccountService {

    @Autowired
    private AccountMapper accountMapper;

    /**
     * 列表数据
     * @param account
     * @param pageInfo
     * @return
     */
    @Override
    public PageInfo list(Account account, PageInfo pageInfo) {
        account.setIsDel(SysParamDetailConstant.IS_DEL_FALSE);

        AccountExample example = getCondition(account);

        if(null != pageInfo && null!= pageInfo.getPageStart()) {
            example.setLimitClauseStart(pageInfo.getPageStart());
            example.setLimitClauseCount(pageInfo.getPageCount());
        }

        List<Account> list = accountMapper.selectByExample(example);
        int totalCount = accountMapper.countByExample(example);

        pageInfo.setList(list);
        pageInfo.setTotalCount(totalCount);

        return pageInfo;
    }

    /**
     * 数据查询
     * @param account
     * @return
     */
    @Override
    public List<Account> list(Account account) {
        AccountExample example = getCondition(account);

        List<Account> list = accountMapper.selectByExample(example);

        return list;
    }

    /**
     * 新增
     * @param account
     */
    @Override
    public void add(Account account) {
        AccountExample example = new AccountExample();
        example.createCriteria().andPhoneNoEqualTo(account.getPhoneNo());

        List<Account> list = accountMapper.selectByExample(example);
        if(CollectionUtils.isNotEmpty(list)) {
            throw new ApplicationException("手机号码:" + account.getPhoneNo() + ",已存在");
        }

        // MD5加密默认密码
        account.setPassword(EncryptFactory.getInstance(SysParamDetailConstant.MD5).encodePassword(CommonConstant.DEFAULT_PWD, CommonConstant.SALT));

        // 状态为新增
        account.setStatus(SysParamDetailConstant.ACCOUNT_STATUS_NEW);
        // 默认为分红
        account.setIsReward(SysParamDetailConstant.IS_REWARD_TRUE);
        // 积分为0
        account.setPoint("0");
        account.setIsDel(SysParamDetailConstant.IS_DEL_FALSE);
        account.setCreateTime(DateUtil.getTimestamp());

        accountMapper.insertSelective(account);
    }

    /**
     * 根据主键获取
     * @param account
     * @return
     */
    @Override
    public Account get(Account account) {
        account = accountMapper.selectByPrimaryKey(account.getId());

        return account;
    }

    /**
     * 保存修改
     * @param account
     */
    @Override
    public void modify(Account account) {
        accountMapper.updateByPrimaryKeySelective(account);
    }

    /**
     * 删除
     * @param account
     */
    @Override
    public void delete(Account account) {
        account.setIsDel(SysParamDetailConstant.IS_DEL_TRUE);

        accountMapper.updateByPrimaryKeySelective(account);
    }

    /**
     * 注册
     * @param account
     * @return
     */
    @Override
    public Account register(Account account) {
        AccountExample example = new AccountExample();
        example.createCriteria().andPhoneNoEqualTo(account.getPhoneNo());

        List<Account> list = accountMapper.selectByExample(example);
        if(CollectionUtils.isNotEmpty(list)) {
            throw new ApplicationException("手机号码:" + account.getPhoneNo() + ",已存在");
        }

        // MD5加密默认密码
        account.setPassword(EncryptFactory.getInstance(SysParamDetailConstant.MD5).encodePassword(CommonConstant.DEFAULT_PWD, CommonConstant.SALT));

        // 状态为新增
        account.setStatus(SysParamDetailConstant.ACCOUNT_STATUS_NEW);
        // 默认为分红
        account.setIsReward(SysParamDetailConstant.IS_REWARD_TRUE);
        // 积分为0
        account.setPoint("0");
        account.setIsDel(SysParamDetailConstant.IS_DEL_FALSE);
        account.setCreateTime(DateUtil.getTimestamp());

        accountMapper.insertSelective(account);

        return account;
    }

    /**
     * 组装查询条件
     * @param account
     * @return
     */
    private AccountExample getCondition(Account account) {
        AccountExample example = new AccountExample();

        if(null != account) {
            AccountExample.Criteria criteria = example.createCriteria();

            if(StringUtil.isNotEmptyObject(account.getType())) {
                criteria.andTypeEqualTo(account.getType());
            }

            if(StringUtil.isNotEmptyObject(account.getIsDel())) {
                criteria.andIsDelEqualTo(account.getIsDel());
            }

            if(StringUtil.isNotEmptyObject(account.getPhoneNo())) {
                criteria.andPhoneNoLike("%" + account.getPhoneNo() + "%");
            }

            if(StringUtil.isNotEmptyObject(account.getStatus())) {
                criteria.andStatusEqualTo(account.getStatus());
            }
        }

        return example;
    }
}
