package cn.wolfcode.p2p.business.mapper;

import cn.wolfcode.p2p.business.domain.SystemAccountFlow;
import java.util.List;

public interface SystemAccountFlowMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SystemAccountFlow record);

    SystemAccountFlow selectByPrimaryKey(Long id);

    List<SystemAccountFlow> selectAll();

    int updateByPrimaryKey(SystemAccountFlow record);
}