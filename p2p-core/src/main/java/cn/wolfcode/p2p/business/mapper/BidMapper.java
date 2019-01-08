package cn.wolfcode.p2p.business.mapper;

import cn.wolfcode.p2p.business.domain.Bid;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BidMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Bid record);

    Bid selectByPrimaryKey(Long id);

    List<Bid> selectAll();

    int updateByPrimaryKey(Bid record);

    BigDecimal selectTotalBidByBidRequestIdAndBidId(@Param("userId") Long userId, @Param("bidRequestId") Long bidRequestId);

    void updateStateByBidRequestId(@Param("bidRequestId") Long bidRequestId, @Param("bidRequestState") int bidRequestState);

    BigDecimal selectBidAmountByBidRequestIdAndUserId(@Param("bidRequestId") Long bidRequestId, @Param("userId") Long userId);

}