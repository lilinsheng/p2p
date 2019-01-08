package cn.wolfcode.p2p.base.mapper;

import cn.wolfcode.p2p.base.domain.IpLog;
import cn.wolfcode.p2p.base.query.IpLogQueryObject;

import java.util.Date;
import java.util.List;

public interface IpLogMapper {

    int insert(IpLog record);

    Date selectLastLoginTime(String username);

    int selectForCount(IpLogQueryObject qo);

    List selectForList(IpLogQueryObject qo);
}