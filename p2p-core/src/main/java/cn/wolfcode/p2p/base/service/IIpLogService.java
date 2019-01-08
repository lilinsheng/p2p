package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.query.IpLogQueryObject;
import cn.wolfcode.p2p.util.PageResult;

import java.util.Date;

public interface IIpLogService {
    /**
     * 添加日志
     */
    void save(String username,Integer userType, int state);

    /**
     * 获取最后登录时间
     */
    Date getLastLoginTime(String username);

    /**
     * 前台查询日志列表
     */
    PageResult queryForPage(IpLogQueryObject qo);
}
