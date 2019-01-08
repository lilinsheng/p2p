package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.business.domain.ExpAccount;
import cn.wolfcode.p2p.business.domain.ExpAccountGrantRecord;

import java.math.BigDecimal;

public interface IExpAccountFlowService {
    /**
     * 体验金流水
     * @param type ： 类型
     * @param amount ：金额
     * @param note ：备注
     * @param expAccount ：体验金账户
     */
    void createExpAccountFlow(Integer type, BigDecimal amount, String note, ExpAccount expAccount);


}
