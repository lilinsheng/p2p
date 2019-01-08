package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.mapper.AccountMapper;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements IAccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public Account getById(Long id) {
        return accountMapper.selectByPrimaryKey(id);
    }

    @Override
    public void save(Account account) {
        accountMapper.insert(account);
    }

    @Override
    public void update(Account account) {
        Assert.isFalse(0==accountMapper.updateByPrimaryKey(account),"丢失更新");
    }
}
