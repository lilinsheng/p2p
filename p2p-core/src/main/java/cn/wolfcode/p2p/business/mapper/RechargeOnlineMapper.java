package cn.wolfcode.p2p.business.mapper;

import cn.wolfcode.p2p.business.domain.RechargeOnline;
import java.util.List;

public interface RechargeOnlineMapper {

    int insert(RechargeOnline record);

    RechargeOnline selectByPrimaryKey(Long id);

    int updateByPrimaryKey(RechargeOnline record);

    RechargeOnline selectByTradeCode(String tradeCode);
}