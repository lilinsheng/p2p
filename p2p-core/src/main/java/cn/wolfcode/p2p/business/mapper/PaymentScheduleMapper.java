package cn.wolfcode.p2p.business.mapper;

import cn.wolfcode.p2p.business.domain.PaymentSchedule;
import cn.wolfcode.p2p.business.query.PaymentScheduleQueryObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PaymentScheduleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PaymentSchedule record);

    PaymentSchedule selectByPrimaryKey(Long id);

    List<PaymentSchedule> selectAll();

    int updateByPrimaryKey(PaymentSchedule record);

    int selectForCount(PaymentScheduleQueryObject qo);

    List selectForList(PaymentScheduleQueryObject qo);

    int selectReturnNumber(@Param("bidRequestId") Long bidRequestId, @Param("state") int state);

    List selectNoReturnExpGold(PaymentScheduleQueryObject qo);
}