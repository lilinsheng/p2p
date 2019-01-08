package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.business.domain.ExpAccountGrantRecord;

import java.util.List;

public interface IExpAccountGrantRecordService {
    /**
     * 保存体验金领取记录
     */
    void save(ExpAccountGrantRecord record);

    /**
     * 根据id查询
     */
    ExpAccountGrantRecord getById(Long id);

    /**
     * 根据平台用户id
     */
    List<ExpAccountGrantRecord> getByUserId(Long userId);

    /**
     * 体验金到期归还系统
     */
    void returnSystemExpGold();

}
