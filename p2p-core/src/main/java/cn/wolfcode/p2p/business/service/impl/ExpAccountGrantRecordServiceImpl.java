package cn.wolfcode.p2p.business.service.impl;

import cn.wolfcode.p2p.business.domain.ExpAccount;
import cn.wolfcode.p2p.business.domain.ExpAccountGrantRecord;
import cn.wolfcode.p2p.business.mapper.ExpAccountGrantRecordMapper;
import cn.wolfcode.p2p.business.service.IExpAccountFlowService;
import cn.wolfcode.p2p.business.service.IExpAccountGrantRecordService;
import cn.wolfcode.p2p.business.service.IExpAccountService;
import cn.wolfcode.p2p.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ExpAccountGrantRecordServiceImpl implements IExpAccountGrantRecordService {
    @Autowired
    private ExpAccountGrantRecordMapper expAccountGrantRecordMapper;
    @Autowired
    private IExpAccountService expAccountService;
    @Autowired
    private IExpAccountFlowService expAccountFlowService;

    @Override
    public void save(ExpAccountGrantRecord record) {
        expAccountGrantRecordMapper.insert(record);
    }

    @Override
    public ExpAccountGrantRecord getById(Long id) {
        return expAccountGrantRecordMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<ExpAccountGrantRecord> getByUserId(Long userId) {
        return expAccountGrantRecordMapper.selectByUserId(userId);
    }

    @Override
    public void returnSystemExpGold() {
        //查询待还的体验金
        List<ExpAccountGrantRecord> records = expAccountGrantRecordMapper.selectUnReturn();
        for (ExpAccountGrantRecord record:records){
            //归还体验金
            ExpAccount expAccount = expAccountService.getById(record.getGrantUser().getId());
            expAccount.setUsableAmount(expAccount.getUsableAmount().subtract(record.getAmount()));
            //生成流水
            expAccountFlowService.createExpAccountFlow(Constants.EXPGOLD_FLOW_RETURN_SYSTEM,
                    record.getAmount(),
                    "归还系统体验金:"+record.getAmount(),
                    expAccount);
            expAccountService.update(expAccount);

            record.setState(ExpAccountGrantRecord.STATE_RETURNED);
            update(record);
        }
    }

    private void update(ExpAccountGrantRecord record) {
        expAccountGrantRecordMapper.updateByPrimaryKey(record);
    }
}
