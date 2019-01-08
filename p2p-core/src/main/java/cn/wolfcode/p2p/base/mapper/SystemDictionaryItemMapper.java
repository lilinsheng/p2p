package cn.wolfcode.p2p.base.mapper;

import cn.wolfcode.p2p.base.domain.SystemDictionaryItem;
import java.util.List;

public interface SystemDictionaryItemMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SystemDictionaryItem record);

    SystemDictionaryItem selectByPrimaryKey(Long id);

    int updateByPrimaryKey(SystemDictionaryItem record);

    List<SystemDictionaryItem> selectItemBySn(String educationBackgrounds);
}