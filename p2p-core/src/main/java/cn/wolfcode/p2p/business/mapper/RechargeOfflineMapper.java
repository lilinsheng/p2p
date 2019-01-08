package cn.wolfcode.p2p.business.mapper;

import cn.wolfcode.p2p.business.domain.RechargeOffline;
import cn.wolfcode.p2p.business.query.RechargeOfflineQueryObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RechargeOfflineMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RechargeOffline record);

    RechargeOffline selectByPrimaryKey(Long id);

    List<RechargeOffline> selectAll();

    int updateByPrimaryKey(RechargeOffline record);

    int selectByTradeCodeAndState(@Param("tradeCode") String tradeCode, @Param("state") int state);

    int selectForCount(RechargeOfflineQueryObject qo);

    List selectForList(RechargeOfflineQueryObject qo);
}