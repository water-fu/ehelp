package com.wisdom.service.service.user;

import com.wisdom.common.entity.PageInfo;
import com.wisdom.dao.entity.Account;

import java.util.List;

/**
 * 账户管理
 * Created by fusj on 16/3/14.
 */
public interface IAccountService {
    /**
     * 列表数据
     * @param account
     * @param pageInfo
     * @return
     */
    PageInfo list(Account account, PageInfo pageInfo);

    /**
     * 数据查询
     * @param account
     * @return
     */
    List<Account> list(Account account);

    /**
     * 新增
     * @param account
     */
    void add(Account account);

    /**
     * 根据主键获取
     * @param account
     * @return
     */
    Account get(Account account);

    /**
     * 保存修改
     * @param account
     */
    void modify(Account account);

    /**
     * 删除
     * @param account
     */
    void delete(Account account);

    /**
     * 注册
     * @param account
     * @return
     */
    Account register(Account account);
}
