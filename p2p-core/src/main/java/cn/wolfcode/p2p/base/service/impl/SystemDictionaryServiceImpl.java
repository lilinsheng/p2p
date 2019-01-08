package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.SystemDictionaryItem;
import cn.wolfcode.p2p.base.mapper.SystemDictionaryItemMapper;
import cn.wolfcode.p2p.base.mapper.SystemDictionaryMapper;
import cn.wolfcode.p2p.base.service.ISystemDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemDictionaryServiceImpl implements ISystemDictionaryService {
    @Autowired
    private SystemDictionaryMapper systemDictionaryMapper;

    @Autowired
    private SystemDictionaryItemMapper systemDictionaryItemMapper;

    @Override
    public List<SystemDictionaryItem> getBySn(String educationBackgrounds) {
        return systemDictionaryItemMapper.selectItemBySn(educationBackgrounds);
    }
}
