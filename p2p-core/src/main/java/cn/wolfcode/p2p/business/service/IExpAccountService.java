package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.business.domain.ExpAccount;

public interface IExpAccountService {
    /**
     * 根据id查询体验金的账户
     */
    ExpAccount getById(Long id);

    /**
     * 获取体验金
     */
    void getExpGold();

    /**
     * 更新体验金账户
     */
    void update(ExpAccount expAccount);
}
