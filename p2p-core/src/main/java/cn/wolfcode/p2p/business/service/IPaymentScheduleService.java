package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.business.domain.BidRequest;
import cn.wolfcode.p2p.business.domain.PaymentSchedule;
import cn.wolfcode.p2p.business.query.PaymentScheduleQueryObject;
import cn.wolfcode.p2p.util.PageResult;

import java.util.List;

public interface IPaymentScheduleService {
    /**
     * 创建还款计划
     * @param bidRequest：借款对象
     */
    void createPaymentSchedule(BidRequest bidRequest);

    /**
     * 分页查询还款计划列表
     * @param qo
     * @return
     */
    PageResult queryForPage(PaymentScheduleQueryObject qo);

    /**
     * 根据id查询还款对象
     */
    PaymentSchedule getById(Long id);

    /**
     * 查询还款的次数
     * @param bidRequestId ：借款对象id
     * @param state ：状态
     * @return
     */
    int selectReturnNumber(Long bidRequestId, int state);

    /**
     * 更新还款计划
     * @param ps
     */
    void update(PaymentSchedule ps);

    /**
     * 定时查询逾期用户，添加滞纳金
     */
    List<PaymentSchedule> queryForOverdue(PaymentScheduleQueryObject qo);

    /**
     * 体验金还款
     */
    void returnExpGold();
}
