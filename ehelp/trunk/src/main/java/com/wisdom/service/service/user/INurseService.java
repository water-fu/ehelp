package com.wisdom.service.service.user;

import com.wisdom.dao.entity.Nurse;

/**
 * 护士详细信息
 * Created by fusj on 16/3/14.
 */
public interface INurseService {
    /**
     * 根据accountId获取护士信息
     * @param nurse
     * @return
     */
    Nurse getByAccountId(Nurse nurse);
}
