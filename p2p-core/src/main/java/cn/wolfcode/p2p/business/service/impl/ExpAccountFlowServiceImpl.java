package cn.wolfcode.p2p.business.service.impl;
import java.util.Date;

import cn.wolfcode.p2p.business.domain.ExpAccount;
import cn.wolfcode.p2p.business.domain.ExpAccountFlow;
import cn.wolfcode.p2p.business.domain.ExpAccountGrantRecord;
import cn.wolfcode.p2p.business.mapper.ExpAccountFlowMapper;
import cn.wolfcode.p2p.business.service.IExpAccountFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class ExpAccountFlowServiceImpl implements IExpAccountFlowService {

    @Autowired
    private ExpAccountFlowMapper expAccountFlowMapper;

    @Override
    public void createExpAccountFlow(Integer type, BigDecimal amount, String note, ExpAccount expAccount) {
        ExpAccountFlow flow = new ExpAccountFlow();
        flow.setActionType(type);
        flow.setAmount(amount);
        flow.setNote(note);
        flow.setUsableAmount(expAccount.getUsableAmount());
        flow.setFreezedAmount(expAccount.getFreezedAmount());
        flow.setActionTime(new Date());
        flow.setExpAccount(expAccount);

        expAccountFlowMapper.insert(flow);
    }

}
