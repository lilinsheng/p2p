package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.SystemDictionaryItem;

import java.util.List;

public interface ISystemDictionaryService {
    /**
     * 查询明细
     */
    List<SystemDictionaryItem> getBySn(String educationBackgrounds);
}
