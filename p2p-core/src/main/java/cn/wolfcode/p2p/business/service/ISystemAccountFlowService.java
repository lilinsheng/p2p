package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.business.domain.SystemAccount;

import java.math.BigDecimal;

public interface ISystemAccountFlowService {
    /**
     * 系统账户流水
     * @param type 类型
     * @param amount 金额
     * @param note 备注
     * @param systemAccount 系统账户
     */
    void createFlow(Integer type, BigDecimal amount, String note, SystemAccount systemAccount);
}
