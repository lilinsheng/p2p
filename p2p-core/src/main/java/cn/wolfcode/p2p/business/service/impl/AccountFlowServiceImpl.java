package cn.wolfcode.p2p.business.service.impl;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.business.domain.AccountFlow;
import cn.wolfcode.p2p.business.mapper.AccountFlowMapper;
import cn.wolfcode.p2p.business.service.IAccountFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class AccountFlowServiceImpl implements IAccountFlowService {

    @Autowired
    private AccountFlowMapper accountFlowMapper;

    @Override
    public void save(AccountFlow flow) {
        accountFlowMapper.insert(flow);
    }

    @Override
    public void createAccountFlow(Integer type, BigDecimal amount, String remark, Account account) {
        AccountFlow flow = new AccountFlow();
        flow.setActionType(type);
        flow.setAmount(amount);
        flow.setNote(remark);
        flow.setUsableAmount(account.getUsableAmount());
        flow.setFreezedAmount(account.getFreezedAmount());
        flow.setActionTime(new Date());
        flow.setAccountId(account.getId());
        accountFlowMapper.insert(flow);
    }
}
