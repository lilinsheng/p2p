package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.business.domain.SystemAccount;

public interface ISystemAccountService {
    /**
     * 获取系统账户户
     * @return
     */
    SystemAccount getAccount();

    /**
     * 更新系统账户
     * @param systemAccount
     */
    void update(SystemAccount systemAccount);
}
