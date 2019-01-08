package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.business.domain.RechargeOnline;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

public interface IRechargeOnlineService {
    /**
     * 保存线上充值记录
     */
    void save(RechargeOnline rechargeOnline);

    /**
     * 根据流水号获取充值记录
     */
    RechargeOnline getByTradeCode(String tradeCode);

    /**
     * 系统异步回调通知地址，结果处理
     */
    String notifySuccess(HttpServletRequest request);

    /**
     * 更新线上充值记录
     */
    void update(RechargeOnline ro);
}
