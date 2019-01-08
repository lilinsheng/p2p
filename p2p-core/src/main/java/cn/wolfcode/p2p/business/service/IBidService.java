package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.business.domain.BidRequest;
import cn.wolfcode.p2p.util.PageResult;

import java.math.BigDecimal;

public interface IBidService {

    /**
     * 投资
     * @param bidRequestId 标的id
     * @param amount 投资金额
     */
    void bid(Long bidRequestId, BigDecimal amount);

    /**
     * 修改投标记录中的标记录
     * @param bidRequestId
     * @param bidRequestState
     */
    void updateStateByBidRequestId(Long bidRequestId, int bidRequestState);

    /**
     * 查询用户对某个借款的总投资
     * @param bidRequestId ：借款对象id
     * @param userId ：投资人id
     * @return
     */
    BigDecimal getBidAmountByBidRequestIdAndUserId(Long bidRequestId, Long userId);

    /**
     * 还款
     * @param id ：还款计划的id
     */
    void returnMoney(Long id);

}
