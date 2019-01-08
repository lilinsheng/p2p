package cn.wolfcode.p2p.business.mapper;

import cn.wolfcode.p2p.business.domain.PaymentScheduleDetail;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentScheduleDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PaymentScheduleDetail record);

    PaymentScheduleDetail selectByPrimaryKey(Long id);

    List<PaymentScheduleDetail> selectAll();

    int updateByPrimaryKey(PaymentScheduleDetail record);

    List<PaymentScheduleDetail> selectDetailsByPsIdAndBidRequestId(@Param("psId") Long psId, @Param("bidRequestId") Long bidRequestId);

    BigDecimal selectUnReceivePrincipal(@Param("bidRequestId") Long bidRequestId, @Param("bidUserId") Long bidUserId);

    BigDecimal selectUnReceiveInterest(@Param("bidRequestId") Long bidRequestId, @Param("bidUserId") Long bidUserId);

    /**
     * 批量修改收款记录的债权转让状态
     * @param bidId ：投资记录id
     * @param toLoginInfoId ：收款人id
     * @param transFerState ：债权转让状态
     */
    void batchTransFerStateByBidIdAndToLoginInfoId(@Param("bidId") Long bidId, @Param("toLoginInfoId") Long toLoginInfoId, @Param("transFerState") Integer transFerState);

    void batchUpdateToLoginInfoBy(@Param("bidId") Long bidId, @Param("toLoginInfoId") Long toLoginInfoId);

    void batchTransFerStateByBidRequestId(Long bidRequestId);

    BigDecimal selectrincipalByBidId(Long bidId);
}