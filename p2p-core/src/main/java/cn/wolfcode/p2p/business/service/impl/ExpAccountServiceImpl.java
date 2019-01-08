package cn.wolfcode.p2p.business.service.impl;
import java.util.Date;
import java.math.BigDecimal;

import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.base.domain.UserInfo;
import cn.wolfcode.p2p.base.service.IUserInfoService;
import cn.wolfcode.p2p.business.domain.ExpAccount;
import cn.wolfcode.p2p.business.domain.ExpAccountGrantRecord;
import cn.wolfcode.p2p.business.mapper.ExpAccountMapper;
import cn.wolfcode.p2p.business.service.IExpAccountFlowService;
import cn.wolfcode.p2p.business.service.IExpAccountGrantRecordService;
import cn.wolfcode.p2p.business.service.IExpAccountService;
import cn.wolfcode.p2p.util.Assert;
import cn.wolfcode.p2p.util.BitStatesUtils;
import cn.wolfcode.p2p.util.Constants;
import cn.wolfcode.p2p.util.UserContext;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ExpAccountServiceImpl implements IExpAccountService {

    @Autowired
    private ExpAccountMapper expAccountMapper;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IExpAccountFlowService expAccountFlowService;
    @Autowired
    private IExpAccountGrantRecordService expAccountGrantRecordService;

    @Override
    public ExpAccount getById(Long id) {
        return expAccountMapper.selectByPrimaryKey(id);
    }

    @Override
    public void getExpGold() {
        LoginInfo loginInfo = UserContext.getLoginInfo();
        UserInfo userInfo = userInfoService.getById(loginInfo.getId());
        //判断用户有无领取过体验金
        Assert.isFalse(userInfo.hasGetExpGold(),"你已经领取过体验金了");

        ExpAccount expAccount = new ExpAccount();
        expAccount.setId(loginInfo.getId());
        expAccount.setUsableAmount(Constants.EXPGOLD_DEFAULT);
        expAccountMapper.insert(expAccount);

        //创建体验金额流水
        expAccountFlowService.createExpAccountFlow(Constants.EXPGOLD_FLOW_GETEXPGOLD,
                Constants.EXPGOLD_DEFAULT,
                "领取体验金成功，金额："+Constants.EXPGOLD_DEFAULT,
                expAccount);

        //保存体验金获取记录
        ExpAccountGrantRecord record = new ExpAccountGrantRecord();
        record.setGrantUser(loginInfo);
        record.setAmount(Constants.EXPGOLD_DEFAULT);
        record.setGrantDate(new Date());
        record.setReturnDate(DateUtils.addMonths(record.getGrantDate(),1));
        record.setGrantType(ExpAccountGrantRecord.GRANT_TYPE_GIFT);
        record.setNote("系统赠送体验金："+record.getAmount());

        expAccountGrantRecordService.save(record);

        //修改位状态：已领取体验金
        userInfo.addBitState(BitStatesUtils.HAS_GET_EXPGOLD);
        userInfoService.update(userInfo);
    }

    @Override
    public void update(ExpAccount expAccount) {
        Assert.isFalse(0==expAccountMapper.updateByPrimaryKey(expAccount),"体验金账户更新失败[乐观锁]");
    }
}
