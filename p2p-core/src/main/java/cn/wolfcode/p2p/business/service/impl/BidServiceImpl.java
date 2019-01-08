package cn.wolfcode.p2p.business.service.impl;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.business.domain.*;
import cn.wolfcode.p2p.business.mapper.BidMapper;
import cn.wolfcode.p2p.business.service.*;
import cn.wolfcode.p2p.util.Assert;
import cn.wolfcode.p2p.util.CalculatetUtil;
import cn.wolfcode.p2p.util.Constants;
import cn.wolfcode.p2p.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.wolfcode.p2p.business.domain.AccountFlow.*;
import static cn.wolfcode.p2p.util.Constants.*;

@Service
public class BidServiceImpl implements IBidService {

    @Autowired
    private BidMapper bidMapper;
    @Autowired
    private IBidRequestService bidRequestService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IAccountFlowService accountFlowService;
    @Autowired
    private IPaymentScheduleService paymentScheduleService;
    @Autowired
    private IPaymentScheduleDetailService paymentScheduleDetailService;
    @Autowired
    private ISystemAccountService systemAccountService;
    @Autowired
    private ISystemAccountFlowService systemAccountFlowService;
    @Autowired
    private ICreditTransferService creditTransferService;
    @Autowired
    private IExpAccountService expAccountService;
    @Autowired
    private IExpAccountFlowService expAccountFlowService;

    @Override
    public void bid(Long bidRequestId, BigDecimal amount) {
        LoginInfo loginInfo = UserContext.getLoginInfo();
        //判断
        //1.判断借款对象状态是否处于招标中
        BidRequest bidRequest = bidRequestService.getById(bidRequestId);
        Assert.isFalse(bidRequest.getBidRequestState()!=BIDREQUEST_STATE_BIDDING,"该标不处于招标中");

        //2.如果剩余可投标小于最小投标
        BigDecimal remainAmount = bidRequest.getRemainAmount();
        if (remainAmount.compareTo(bidRequest.getMinBidAmount())<0){
            //一次性投完
            Assert.isFalse(remainAmount.compareTo(amount)!=0,"当前投标只能投："+remainAmount);
        }else {
            //  投标金额要大于等于最小投标
            Assert.isFalse(amount.compareTo(bidRequest.getMinBidAmount())<0,"最少投标为："+SMALLEST_BID_AMOUNT);
        }

        //3.当前用户对于这个借款的总投标金额不能超过借款金额的 50%
        //当前已投资总额
        BigDecimal totalAmount = bidMapper.selectTotalBidByBidRequestIdAndBidId(UserContext.getLoginInfo().getId(),bidRequestId);
        BigDecimal ableAmount = bidRequest.getBidRequestAmount().multiply(new BigDecimal("0.5"));

        Assert.isFalse(totalAmount.add(amount).compareTo(ableAmount)>=0,"该标你剩余投资不能超过："+ableAmount.subtract(totalAmount));

        //5.当前投标金额不能超过剩余可投标
        Assert.isFalse(amount.compareTo(remainAmount)>0,"当前投标金额不能超过剩余可投标");

        //判断是信用标还是体验标
        if (bidRequest.getBidRequestType()==BidRequest.BID_TYPE_CREDIT){

            //信用标
            //4.判断可用余额要大于等于当前投标金额
            Account account = accountService.getById(UserContext.getLoginInfo().getId());
            Assert.isFalse(account.getUsableAmount().compareTo(amount)<0,"可用余额不足"+amount);

            //6.当前投标
            Assert.isFalse(UserContext.getLoginInfo().getId()==bidRequest.getCreateUser().getId(),"用户不能是借款人");

            //执行投标
            //1.可用余额要减少
            account.setUsableAmount(account.getUsableAmount().subtract(amount));
            //2.冻结金额增加
            account.setFreezedAmount(account.getFreezedAmount().add(amount));
            //3.创建投标冻结流水
            accountFlowService.createAccountFlow(ACCOUNT_ACTIONTYPE_BID_FREEZED,
                    amount,"投标["+bidRequest.getTitle()+"],冻结金额"+amount,account);

            accountService.update(account);
        }else {
            //体验标
            //4.判断可用余额要大于等于当前投标金额
            ExpAccount expAccount = expAccountService.getById(loginInfo.getId());
            Assert.isFalse(expAccount.getUsableAmount().compareTo(amount)<0,"可用余额不足"+amount);

            //执行投标
            //体验金可用余额减少
            expAccount.setUsableAmount(expAccount.getUsableAmount().subtract(amount));
            //创建投标成功流水
            expAccountFlowService.createExpAccountFlow(Constants.EXPGOLD_FLOW_REDUCE_AMOUNT,
                    amount,
                    "["+bidRequest.getTitle()+"]投标成功，余额减少："+amount,
                    expAccount);
            //冻结金额增加
            expAccount.setFreezedAmount(expAccount.getFreezedAmount().add(amount));
            //创建投标成功，冻结增加流水
            expAccountFlowService.createExpAccountFlow(Constants.EXPGOLD_FLOW_ADD_FREE,
                    amount,
                    "["+bidRequest.getTitle()+"]投标成功，冻结金额增加："+amount,
                    expAccount);
            expAccountService.update(expAccount);

        }




        //5.借款对象的 投标次数增加
        bidRequest.setBidCount(bidRequest.getBidCount()+1);

        //6.借款对象的 投标总额增加
        bidRequest.setCurrentSum(bidRequest.getCurrentSum().add(amount));

        //后续
        //如果满标，借款对象状态修改为满标一审
        if (bidRequest.getRemainAmount().intValue()==0){
            bidRequest.setBidRequestState(BIDREQUEST_STATE_APPROVE_PENDING_1);
        }
        //这个借款下的所有投标记录的状态修改为满标一审
        //4.保存一个投标对象
        Bid bid = new Bid();
        bid.setActualRate(bidRequest.getCurrentRate());
        bid.setAvailableAmount(amount);
        bid.setBidRequestId(bidRequestId);
        bid.setBidUser(UserContext.getLoginInfo());
        bid.setBidTime(new Date());
        bid.setBidRequestTitle(bidRequest.getTitle());
        bid.setBidRequestState(bidRequest.getBidRequestState());


        bidRequestService.update(bidRequest);
        bidMapper.insert(bid);
    }

    @Override
    public void updateStateByBidRequestId(Long bidRequestId, int bidRequestState) {
        bidMapper.updateStateByBidRequestId(bidRequestId,bidRequestState);
    }

    @Override
    public BigDecimal getBidAmountByBidRequestIdAndUserId(Long bidRequestId, Long userId) {
        return bidMapper.selectBidAmountByBidRequestIdAndUserId(bidRequestId,userId);
    }

    @Override
    public void returnMoney(Long id) {
        //还款人id
        Long borrowUserId = UserContext.getLoginInfo().getId();
        //还款人账户
        Account borrowUserAccount = accountService.getById(borrowUserId);

        //参数校验
        //获取还款计划对象
        PaymentSchedule ps = paymentScheduleService.getById(id);
        //判断还款计划是否处于待还款或逾期状态
        Assert.isFalse(ps.getState()==PAYMENT_STATE_DONE,"该还款已经还清");
        //判断余额是否大于还款金额
        Assert.isFalse(borrowUserAccount.getUsableAmount().compareTo(ps.getTotalAmount())<0,"余额不足还款");
        //判断当前用户是否是还款人
        Assert.isFalse(borrowUserId!=ps.getBorrowUser().getId(),"不能帮别人还款");

        //-------------还款人-------------
        //还款，还款人余额减少
        borrowUserAccount.setUsableAmount(borrowUserAccount.getUsableAmount().subtract(ps.getTotalAmount()));
        //生成还款流水
        accountFlowService.createAccountFlow(AccountFlow.ACCOUNT_ACTIONTYPE_RETURN_MONEY,
                ps.getTotalAmount(),
                "["+ps.getBidRequestTitle()+"]的第"+ps.getMonthIndex()+"期还款成功，还款："+ps.getTotalAmount(),
                borrowUserAccount);
        //还款人的信用额度增加
        borrowUserAccount.setRemainBorrowLimit(borrowUserAccount.getRemainBorrowLimit().add(ps.getPrincipal()));
        //待还总额减少
        borrowUserAccount.setUnReturnAmount(borrowUserAccount.getUnReturnAmount().subtract(ps.getTotalAmount()));
        //还款计划：保存还款时间，状态
        ps.setPayDate(new Date());
        ps.setState(PAYMENT_STATE_DONE);

        accountService.update(borrowUserAccount);

        //---------收款人------------
        //查询对应的收款计划
        List<PaymentScheduleDetail> details = paymentScheduleDetailService.getDetailsByPsIdAndBidRequestId(id,ps.getBidRequestId());
        Map<Long,Account> accounts = new HashMap<>();
        SystemAccount systemAccount = systemAccountService.getAccount();
        for (PaymentScheduleDetail detail : details){
            Long bidUserId = detail.getToLoginInfoId();
            //收款人账户
            Account bidAccount = accounts.get(bidUserId);
            if (bidAccount==null){
                bidAccount = accountService.getById(bidUserId);
                accounts.put(bidUserId,bidAccount);
            }
            //收款：可用余额增加
            bidAccount.setUsableAmount(bidAccount.getUsableAmount().add(detail.getTotalAmount()));
            //生成流水
            accountFlowService.createAccountFlow(AccountFlow.ACCOUNT_ACTIONTYPE_CALLBACK_MONEY,
                    detail.getTotalAmount(),
                    "["+ps.getBidRequestTitle()+"]的第"+detail.getMonthIndex()+"期回款成功，回款："+detail.getTotalAmount(),
                    bidAccount);
            //代收本金减少
            bidAccount.setUnReceivePrincipal(bidAccount.getUnReceivePrincipal().subtract(detail.getPrincipal()));
            //代收利息减少
            bidAccount.setUnReceiveInterest(bidAccount.getUnReceiveInterest().subtract(detail.getInterest()));
            //收款计划
            detail.setPayDate(ps.getPayDate());
            paymentScheduleDetailService.update(detail);
            //支付平台利息手续费
            BigDecimal systemInterest = CalculatetUtil.calInterestManagerCharge(detail.getInterest());
            bidAccount.setUsableAmount(bidAccount.getUsableAmount().subtract(systemInterest));
            //生成支付平台利息管理费流水
            accountFlowService.createAccountFlow(AccountFlow.ACCOUNT_ACTIONTYPE_INTEREST_SHARE,
                    systemInterest,
                    "["+ps.getBidRequestTitle()+"]的第"+detail.getMonthIndex()+"期回款成功，支付平台利息管理费："+systemInterest,
                    bidAccount);
            //平台账户收到利息管理费
            systemAccount.setUsableAmount(systemAccount.getUsableAmount().add(systemInterest));
            //生成平台账户流水
            systemAccountFlowService.createFlow(SystemAccountFlow.SYSTEM_ACCOUNT_ACTIONTYPE_INTREST_MANAGE_CHARGE,
                    systemInterest,
                    "收到利息管理费："+systemInterest,
                    systemAccount);
        }

        //撤消转让中的债权标
        Long bidRequestId = ps.getBidRequestId();
        creditTransferService.batchUpdateTransStateInTransingByBidRequestId(bidRequestId);
        //更新收款计划的转让状态
        paymentScheduleDetailService.batchTransFerStateByBidRequestId(bidRequestId);



        //保存收款人账户信息
        for (Account a:accounts.values()){
            accountService.update(a);
        }

        //更新平台账户
        systemAccountService.update(systemAccount);

        //更新还款计划
        paymentScheduleService.update(ps);

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
