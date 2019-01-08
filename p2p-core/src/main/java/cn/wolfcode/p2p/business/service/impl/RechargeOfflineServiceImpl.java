package cn.wolfcode.p2p.business.service.impl;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.business.domain.AccountFlow;
import cn.wolfcode.p2p.business.domain.RechargeOffline;
import cn.wolfcode.p2p.business.mapper.RechargeOfflineMapper;
import cn.wolfcode.p2p.business.query.RechargeOfflineQueryObject;
import cn.wolfcode.p2p.business.service.IAccountFlowService;
import cn.wolfcode.p2p.business.service.IRechargeOfflineService;
import cn.wolfcode.p2p.util.Assert;
import cn.wolfcode.p2p.util.PageResult;
import cn.wolfcode.p2p.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import static cn.wolfcode.p2p.base.domain.BaseAuthDomain.*;

@Service
public class RechargeOfflineServiceImpl implements IRechargeOfflineService {
    @Autowired
    private RechargeOfflineMapper rechargeOfflineMapper;

    @Autowired
    private IAccountService accountService;
    @Autowired
    private IAccountFlowService accountFlowService;

    @Override
    public void recharge(RechargeOffline rechargeOffline) {
        //1.参数校验

        //2.流水号是否已经被使用
        int count = rechargeOfflineMapper.selectByTradeCodeAndState(rechargeOffline.getTradeCode(),STATE_SUCCESS);
        Assert.isFalse(count>0,"流水号已经被使用了");

        //流水号已经被申请
        RechargeOffline ro = new RechargeOffline();
        ro.setState(STATE_NORMAL);
        ro.setApplier(UserContext.getLoginInfo());
        ro.setApplyTime(new Date());
        ro.setTradeCode(rechargeOffline.getTradeCode());
        ro.setTradeTime(rechargeOffline.getTradeTime());
        ro.setAmount(rechargeOffline.getAmount());
        ro.setNote(rechargeOffline.getNote());
        ro.setBankInfo(rechargeOffline.getBankInfo());

        rechargeOfflineMapper.insert(ro);
    }

    @Override
    public PageResult queryForPage(RechargeOfflineQueryObject qo) {
        int count = rechargeOfflineMapper.selectForCount(qo);
        if (count==0){
            return PageResult.empty(qo.getPageSize());
        }

        List listData = rechargeOfflineMapper.selectForList(qo);

        return new PageResult(listData,count,qo.getCurrentPage(),qo.getPageSize());
    }

    @Override
    public void audit(Long id, Integer state, String remark) {
        //参数校验

        //判断状态为待审核
        RechargeOffline rechargeOffline = rechargeOfflineMapper.selectByPrimaryKey(id);
        Assert.isFalse(rechargeOffline.getState()!=RechargeOffline.STATE_NORMAL,"线下充值状态不为待审核");

        //设置审核结果
        rechargeOffline.setRemark(remark);
        rechargeOffline.setState(state);
        rechargeOffline.setAuditor(UserContext.getLoginInfo());
        rechargeOffline.setAuditTime(new Date());


        //审核成功，
        if (state==RechargeOffline.STATE_SUCCESS){
            Account account = accountService.getById(rechargeOffline.getApplier().getId());
            //账户可用余额增加
            account.setUsableAmount(account.getUsableAmount().add(rechargeOffline.getAmount()));



            accountService.update(account);
            //添加充值流水记录
            accountFlowService.createAccountFlow(AccountFlow.ACCOUNT_ACTIONTYPE_RECHARGE_OFFLINE,
                    rechargeOffline.getAmount(),"线下充值："+rechargeOffline.getAmount(),account);

        }

        update(rechargeOffline);

    }

    public void update(RechargeOffline rechargeOffline) {
        rechargeOfflineMapper.updateByPrimaryKey(rechargeOffline);
    }
}
