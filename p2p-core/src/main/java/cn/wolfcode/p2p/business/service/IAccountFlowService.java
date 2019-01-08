package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.business.domain.AccountFlow;

import java.math.BigDecimal;

public interface IAccountFlowService {
    void save(AccountFlow flow);

    /**
     * 创建流水记录
     * @param type 流水类型
     * @param amount 金额
     * @param remark 备注
     * @param account 账户
     */
    void createAccountFlow(Integer type, BigDecimal amount, String remark, Account account);
}
