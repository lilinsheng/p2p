package cn.wolfcode.p2p.business.service.impl;

import cn.wolfcode.p2p.business.domain.SystemAccount;
import cn.wolfcode.p2p.business.domain.SystemAccountFlow;
import cn.wolfcode.p2p.business.mapper.SystemAccountFlowMapper;
import cn.wolfcode.p2p.business.service.ISystemAccountFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service
@Transactional
public class SystemAccountFlowServiceImpl implements ISystemAccountFlowService {
    @Autowired
    private SystemAccountFlowMapper systemAccountFlowMapper;

    @Override
    public void createFlow(Integer type, BigDecimal amount, String note, SystemAccount systemAccount) {
        SystemAccountFlow flow = new SystemAccountFlow();
        flow.setActionType(type);
        flow.setActionTime(new Date());
        flow.setAmount(amount);
        flow.setNote(note);
        flow.setFreezedAmount(systemAccount.getFreezedAmount());
        flow.setUsableAmount(systemAccount.getUsableAmount());
        update(flow);
    }

    private void update(SystemAccountFlow flow) {
        systemAccountFlowMapper.insert(flow);
    }
}
