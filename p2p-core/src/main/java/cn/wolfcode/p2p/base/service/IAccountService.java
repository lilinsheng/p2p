package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.Account;

public interface IAccountService {
    /**
     * 查询账户信息
     */
    Account getById(Long id);

    /**
     * 新增一个账户
     */
    void save(Account account);

    /**
     * 统一更新账户
     * @param account
     */
    void update(Account account);
}
