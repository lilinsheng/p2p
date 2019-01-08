package cn.wolfcode.p2p.business.service.impl;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.exception.CustomException;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.business.domain.AccountFlow;
import cn.wolfcode.p2p.business.domain.RechargeOnline;
import cn.wolfcode.p2p.business.mapper.RechargeOfflineMapper;
import cn.wolfcode.p2p.business.mapper.RechargeOnlineMapper;
import cn.wolfcode.p2p.business.service.IAccountFlowService;
import cn.wolfcode.p2p.business.service.IRechargeOnlineService;
import cn.wolfcode.p2p.hosting.util.HostingUtil;
import cn.wolfcode.p2p.util.Assert;
import cn.wolfcode.p2p.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class RechargeOnlineServiceImpl implements IRechargeOnlineService {
    @Autowired
    private RechargeOnlineMapper rechargeOnlineMapper;
    @Autowired
    private IRechargeOnlineService rechargeOnlineService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IAccountFlowService accountFlowService;

    @Override
    public void save(RechargeOnline rechargeOnline) {
        rechargeOnlineMapper.insert(rechargeOnline);
    }

    @Override
    public RechargeOnline getByTradeCode(String tradeCode) {
        return rechargeOnlineMapper.selectByTradeCode(tradeCode);
    }

    @Override
    public String notifySuccess(HttpServletRequest request) {
        try {
            Enumeration<String> names = request.getParameterNames();
            Map<String,String> map = HostingUtil.getMap(names,request);
            String rechargeState = map.get("deposit_status");
            if ("PROCESSING".equals(rechargeState)){
                return "error";
            }

            RechargeOnline ro = rechargeOnlineMapper.selectByTradeCode(map.get("out_trade_no"));
            if (ro==null){
                log.info("流水号不存在");
                return "success";
            }
            if ("SUCCESS".equals(rechargeState)){
                //回调签名验证
                boolean success = HostingUtil.checkSign(map);

                if (success){
                    log.info("充值回调成功");
                }else {
                    Assert.isFalse(!success,"回签校验失败");
                }

                ro.setState(RechargeOnline.STATE_SUCCESS);
                ro.setTradeTime(new Date());

                //增加用户的可用金额
                Account account = accountService.getById(ro.getApplier().getId());
                account.setUsableAmount(account.getUsableAmount().add(ro.getAmount()));
                accountService.update(account);

                //保存充值成功流水
                accountFlowService.createAccountFlow(AccountFlow.ACCOUNT_ACTIONTYPE_RECHARGE_SUCCESS,
                        ro.getAmount(),
                        "线上充值成功："+ro.getAmount(),
                        account);
            }else if ("FAILED".equals(rechargeState)){
                ro.setRemark(map.get("response_message")+"{"+map.get("response_code")+"}");
            }
            rechargeOnlineService.update(ro);
            return "success";
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }

    @Override
    public void update(RechargeOnline ro) {
        rechargeOnlineMapper.updateByPrimaryKey(ro);
    }
}
