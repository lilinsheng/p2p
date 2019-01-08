package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.business.domain.RechargeOffline;
import cn.wolfcode.p2p.business.query.RechargeOfflineQueryObject;
import cn.wolfcode.p2p.util.PageResult;

public interface IRechargeOfflineService {
    /**
     * 线下充值申请
     * @param rechargeOffline
     */
    void recharge(RechargeOffline rechargeOffline);

    /**
     * 线下充值审核列表
     */
    PageResult queryForPage(RechargeOfflineQueryObject qo);

    /**
     * 线下充值审核
     * @param id
     * @param state
     * @param remark
     */
    void audit(Long id, Integer state, String remark);
}
