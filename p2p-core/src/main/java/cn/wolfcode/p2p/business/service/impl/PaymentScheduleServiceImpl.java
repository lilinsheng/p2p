package cn.wolfcode.p2p.business.service.impl;
import java.util.*;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.exception.CustomException;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.business.domain.*;
import cn.wolfcode.p2p.business.mapper.BidMapper;
import cn.wolfcode.p2p.business.mapper.PaymentScheduleMapper;
import cn.wolfcode.p2p.business.query.PaymentScheduleQueryObject;
import cn.wolfcode.p2p.business.service.*;
import cn.wolfcode.p2p.util.Assert;
import cn.wolfcode.p2p.util.CalculatetUtil;
import cn.wolfcode.p2p.util.Constants;
import cn.wolfcode.p2p.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static cn.wolfcode.p2p.util.Constants.BIDREQUEST_STATE_COMPLETE_PAY_BACK;
import static cn.wolfcode.p2p.util.Constants.PAYMENT_STATE_DONE;

@Slf4j
@Service
@Transactional
public class PaymentScheduleServiceImpl implements IPaymentScheduleService {

    @Autowired
    private PaymentScheduleMapper paymentScheduleMapper;
    @Autowired
    private IPaymentScheduleDetailService paymentScheduleDetailService;
    @Autowired
    private ISystemAccountService systemAccountService;
    @Autowired
    private ISystemAccountFlowService systemAccountFlowService;
    @Autowired
    private IExpAccountService expAccountService;
    @Autowired
    private IExpAccountFlowService expAccountFlowService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IBidRequestService bidRequestService;
    @Autowired
    private IPaymentScheduleService paymentScheduleService;
    @Autowired
    private BidMapper bidMapper;
    @Autowired
    private IExpAccountGrantRecordService expAccountGrantRecordService;

    @Override
    public void createPaymentSchedule(BidRequest bidRequest) {

        //还款期数
        Integer returnMonthes = bidRequest.getReturnMonthes();
        BigDecimal totalAmount = Constants.ZERO;
        BigDecimal totalInterest = Constants.ZERO;
        BigDecimal monthMoney;
        BigDecimal interest;
        for (int i = 0;i<returnMonthes;i++){
            PaymentSchedule ps = new PaymentSchedule();
            ps.setBidRequestId(bidRequest.getId());
            ps.setBidRequestTitle(bidRequest.getTitle());
            ps.setBorrowUser(bidRequest.getCreateUser());
            //设置截止时间
            ps.setDeadLine(DateUtils.addMonths(bidRequest.getPublishTime(),i+1));

            if (i==returnMonthes-1){
                monthMoney = bidRequest.getBidRequestAmount().add(bidRequest.getTotalRewardAmount()).subtract(totalAmount);
                interest = bidRequest.getTotalRewardAmount().subtract(totalInterest);
            }else {
                //每期总还款
                monthMoney= CalculatetUtil.calMonthToReturnMoney(bidRequest.getReturnType(), bidRequest.getBidRequestAmount(), bidRequest.getCurrentRate(),
                        i + 1, bidRequest.getReturnMonthes());
                totalAmount=totalAmount.add(monthMoney);
                //每期利息
                interest = CalculatetUtil.calMonthlyInterest(bidRequest.getReturnType(), bidRequest.getBidRequestAmount(), bidRequest.getCurrentRate(),
                        i + 1, bidRequest.getReturnMonthes());
                totalInterest = totalInterest.add(interest);
            }

            ps.setTotalAmount(monthMoney);

            ps.setInterest(interest);
            //每期本金
            ps.setPrincipal(monthMoney.subtract(interest));
            ps.setMonthIndex(i+1);
            ps.setState(Constants.PAYMENT_STATE_NORMAL);
            ps.setBidRequestType(bidRequest.getBidRequestType());
            ps.setReturnType(bidRequest.getReturnType());

            //保存还款计划对象
            paymentScheduleMapper.insert(ps);

            //创建收款计划
            paymentScheduleDetailService.createPaymentScheduleDetail(ps,bidRequest);
        }
    }

    @Override
    public PageResult queryForPage(PaymentScheduleQueryObject qo) {
        int count = paymentScheduleMapper.selectForCount(qo);
        if (count==0){
            return PageResult.empty(qo.getPageSize());
        }

        List listData = paymentScheduleMapper.selectForList(qo);

        return new PageResult(listData,count,qo.getCurrentPage(),qo.getPageSize());
    }

    @Override
    public PaymentSchedule getById(Long id) {
        return paymentScheduleMapper.selectByPrimaryKey(id);
    }

    @Override
    public int selectReturnNumber(Long bidRequestId, int state) {
        return paymentScheduleMapper.selectReturnNumber(bidRequestId,state);
    }

    @Override
    public void update(PaymentSchedule ps) {
        paymentScheduleMapper.updateByPrimaryKey(ps);
    }

    @Override
    public List<PaymentSchedule> queryForOverdue(PaymentScheduleQueryObject qo) {
        return paymentScheduleMapper.selectForList(qo);
    }

    @Override
    public void returnExpGold() {
        //查询所有未还款的体验金
        PaymentScheduleQueryObject qo = new PaymentScheduleQueryObject();
        qo.setBidRequestType(BidRequest.BID_TYPE_EXP);//体验标状态
        //查询所有未还款的体验标的还款计划
        List<PaymentSchedule> list = paymentScheduleMapper.selectNoReturnExpGold(qo);
        Date now = new Date();
        SystemAccount systemAccount = systemAccountService.getAccount();
        for (PaymentSchedule ps : list){
            if (ps.getDeadLine().compareTo(now)<=0){
                //到达还款时间
                //还款计划：
                ps.setPayDate(now);
                ps.setState(Constants.PAYMENT_STATE_DONE);
                paymentScheduleMapper.updateByPrimaryKey(ps);

                //扣除利息费，从系统账户
                //判断系统账户是否够钱
                if (systemAccount.getUsableAmount().compareTo(ps.getInterest())<0){
                    log.error("系统账户不足支付体验金利息费，请充值");
                    throw new CustomException("系统账户不足支付体验金利息费，请充值");
                }
                systemAccount.setUsableAmount(systemAccount.getUsableAmount().subtract(ps.getInterest()));
                //生成系统流水
                systemAccountFlowService.createFlow(SystemAccountFlow.SYSTEM_ACCOUNT_ACTIONTYPE_EXP_INTEREST,
                        ps.getInterest(),
                        "体验标["+ps.getBidRequestTitle()+"]还款，扣除利息："+ps.getInterest(),
                        systemAccount);

                //修改对应的收款计划
                List<PaymentScheduleDetail> details = paymentScheduleDetailService.getDetailsByPsIdAndBidRequestId(ps.getId(), ps.getBidRequestId());
                Map<Long,ExpAccount> expAccountMap = new HashMap<>();
                Map<Long,Account> accountMap = new HashMap<>();
                for (PaymentScheduleDetail detail : details) {
                    Long bidUserId = detail.getToLoginInfoId();
                    ExpAccount expAccount = expAccountMap.get(bidUserId);
                    if (expAccount==null){
                        expAccount = expAccountService.getById(bidUserId);
                        expAccountMap.put(bidUserId,expAccount);
                    }

                    Account account = accountMap.get(bidUserId);
                    if (account==null){
                        account = accountService.getById(bidUserId);
                        accountMap.put(bidUserId,account);
                    }

                    //投标人体验金账户增加
                    expAccount.setUsableAmount(expAccount.getUsableAmount().add(detail.getPrincipal()));
                    //生成流水
                    expAccountFlowService.createExpAccountFlow(Constants.EXPGOLD_FLOW_RETURN_PRINCIPAL,
                            detail.getPrincipal(),
                            "体验标还款，可用余额增加："+detail.getPrincipal(),
                            expAccount);

                    //未还本金减少
                    expAccount.setUnReturnExpAmount(expAccount.getUnReturnExpAmount().subtract(detail.getPrincipal()));

                    //待收利息减少
                    account.setUnReceiveInterest(account.getUnReceiveInterest().subtract(detail.getInterest()));

                    //收款计划更新
                    detail.setPayDate(now);
                    paymentScheduleDetailService.update(detail);
                }
                Collection<ExpAccount> values = expAccountMap.values();
                for (ExpAccount value : values) {
                    expAccountService.update(value);

                    /*//还款成功后，判断冻结金额是否够体验金归还系统
                    List<ExpAccountGrantRecord> records = expAccountGrantRecordService.getByUserId(value.getId());

                    //归还总额
                    BigDecimal returnAmount = Constants.ZERO;
                    for (ExpAccountGrantRecord record : records){
                        if (record.getGrantDate().compareTo(now)<=0){
                            returnAmount.add(record.getAmount());
                        }
                    }*/
                }

                Collection<Account> values1 = accountMap.values();
                for (Account account : values1) {
                    accountService.update(account);
                }



                //还款成功后，判断该借款对象是否已经还清，还清，修改状态
                BidRequest bidRequest = bidRequestService.getById(ps.getBidRequestId());
                int count = paymentScheduleService.selectReturnNumber(ps.getBidRequestId(),PAYMENT_STATE_DONE);
                if (bidRequest.getReturnMonthes()==count){
                    //还清该借款对象
                    bidRequest.setBidRequestState(BIDREQUEST_STATE_COMPLETE_PAY_BACK);
                    bidRequestService.update(bidRequest);
                    //修改投标对象的状态
                    bidMapper.updateStateByBidRequestId(bidRequest.getId(),BIDREQUEST_STATE_COMPLETE_PAY_BACK);
                }

            }
        }
    }
}
