package cn.wolfcode.p2p.business.service.impl;

import cn.wolfcode.p2p.business.domain.SystemAccount;
import cn.wolfcode.p2p.business.mapper.SystemAccountMapper;
import cn.wolfcode.p2p.business.service.ISystemAccountService;
import cn.wolfcode.p2p.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SystemAccountServiceImpl implements ISystemAccountService {
    @Autowired
    private SystemAccountMapper systemAccountMapper;

    @Override
    public SystemAccount getAccount() {
        return systemAccountMapper.selectAccount();
    }

    @Override
    public void update(SystemAccount systemAccount) {
        Assert.isFalse(0==systemAccountMapper.updateByPrimaryKey(systemAccount),"丢失更新");
    }
}
