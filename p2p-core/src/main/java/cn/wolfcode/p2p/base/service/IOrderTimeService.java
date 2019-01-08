package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.OrderTime;

import java.util.List;

public interface IOrderTimeService {
    /**
     * 获取所有的预约时间段
     * @return
     */
    List<OrderTime> listAll();

}
