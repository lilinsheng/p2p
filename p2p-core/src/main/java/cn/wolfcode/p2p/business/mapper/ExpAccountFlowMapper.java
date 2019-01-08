package cn.wolfcode.p2p.business.mapper;

import cn.wolfcode.p2p.business.domain.ExpAccountFlow;
import java.util.List;

public interface ExpAccountFlowMapper {

    int insert(ExpAccountFlow record);

    List<ExpAccountFlow> selectAll();
}