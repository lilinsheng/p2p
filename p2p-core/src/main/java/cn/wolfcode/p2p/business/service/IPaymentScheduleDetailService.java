package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.business.domain.BidRequest;
import cn.wolfcode.p2p.business.domain.PaymentSchedule;
import cn.wolfcode.p2p.business.domain.PaymentScheduleDetail;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface IPaymentScheduleDetailService {
    /**
     * 创建收款计划
     * @param ps ：对应还款计划
     * @param bidRequest ：对应借款对象
     */
    void createPaymentScheduleDetail(PaymentSchedule ps, BidRequest bidRequest);

    /**
     * 根据还款计划id和借款id查询收款计划
     * @param psId ：还款计划id
     * @param bidRequestId ：借款id
     */
    List<PaymentScheduleDetail> getDetailsByPsIdAndBidRequestId(Long psId, Long bidRequestId);

    /**
     * 更新收款计划
     * @param detail
     */
    void update(PaymentScheduleDetail detail);

    /**
     * 查询某投资人对某借款的待收本金
     * @param bidRequestId
     * @param bidUserId
     * @return
     */
    BigDecimal selectUnReceivePrincipal(Long bidRequestId, Long bidUserId);

    /**
     * 查询某投资人对某借款的待收利息
     * @param bidRequestId
     * @param bidUserId
     * @return
     */
    BigDecimal selectUnReceiveInterest(Long bidRequestId, Long bidUserId);

    /**
     * 批量更新收款记录的债权转让状态
     */
    void batchTransFerStateByBidIdAndToLoginInfoId(Long bidId,Long toLoginInfoId,Integer transFerState);

    /**
     * 批量修改收款计划的收款人
     */
    void batchUpdateToLoginInfoBy(Long bidId, Long toLoginInfoId);

    void batchTransFerStateByBidRequestId(Long bidRequestId);

    /**
     * 根据投资id查询收款计划的本金和
     */
    BigDecimal getPrincipalByBidId(Long bidId);
}
