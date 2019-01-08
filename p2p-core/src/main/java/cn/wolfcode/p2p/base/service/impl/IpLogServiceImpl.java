package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.IpLog;
import cn.wolfcode.p2p.base.mapper.IpLogMapper;
import cn.wolfcode.p2p.base.query.IpLogQueryObject;
import cn.wolfcode.p2p.base.service.IIpLogService;
import cn.wolfcode.p2p.util.PageResult;
import cn.wolfcode.p2p.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class IpLogServiceImpl implements IIpLogService {
    @Autowired
    private IpLogMapper ipLogMapper;

    @Override
    public void save(String username,Integer userType, int state) {
        IpLog ipLog = new IpLog();
        ipLog.setIp(UserContext.getIP());
        ipLog.setUsername(username);
        ipLog.setState((byte)state);
        ipLog.setLoginTime(new Date());
        ipLog.setUserType(userType);
        ipLogMapper.insert(ipLog);
    }

    @Override
    public Date getLastLoginTime(String username) {
        return ipLogMapper.selectLastLoginTime(username);
    }

    @Override
    public PageResult queryForPage(IpLogQueryObject qo) {
        int count = ipLogMapper.selectForCount(qo);
        if (count==0){
            return PageResult.empty(qo.getPageSize());
        }

        List listData = ipLogMapper.selectForList(qo);

        return new PageResult(listData,count,qo.getCurrentPage(),qo.getPageSize());
    }
}
