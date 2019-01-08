package cn.wolfcode.p2p.business.service.impl;
import java.util.Date;
import cn.wolfcode.p2p.business.domain.Bid;
import java.util.ArrayList;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.base.domain.UserInfo;
import cn.wolfcode.p2p.base.mapper.AccountMapper;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.service.IUserInfoService;
import cn.wolfcode.p2p.business.domain.*;
import cn.wolfcode.p2p.business.mapper.BidRequestAuditHistoryMapper;
import cn.wolfcode.p2p.business.mapper.BidRequestMapper;
import cn.wolfcode.p2p.business.query.BidRequestQueryObject;
import cn.wolfcode.p2p.business.service.*;
import cn.wolfcode.p2p.util.*;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import static cn.wolfcode.p2p.business.domain.SystemAccountFlow.SYSTEM_ACCOUNT_ACTIONTYPE_MANAGE_CHARGE;
import static cn.wolfcode.p2p.util.Constants.*;

@Service
@Transactional
public class BidRequestServiceImpl implements IBidRequestService {

    //保存待发布借款的队列
    public static final ConcurrentLinkedQueue con = new ConcurrentLinkedQueue();

    @Autowired
    private BidRequestMapper bidRequestMapper;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private BidRequestAuditHistoryMapper bidRequestAuditHistoryMapper;
    @Autowired
    private IBidService bidService;
    @Autowired
    private IAccountFlowService accountFlowService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private ISystemAccountService systemAccountService;
    @Autowired
    private ISystemAccountFlowService systemAccountFlowService;
    @Autowired
    private IPaymentScheduleService paymentScheduleService;
    @Autowired
    private IPaymentScheduleDetailService paymentScheduleDetailService;
    @Autowired
    private IExpAccountService expAccountService;
    @Autowired
    private IExpAccountFlowService expAccountFlowService;

    @Override
    public void apply(BidRequest bidRequest) {
        //1.参数校验
        //借款金额
        Assert.isFalse(bidRequest.getBidRequestAmount().compareTo(SMALLEST_BIDREQUEST_AMOUNT)<0,
                "借款金额少于"+SMALLEST_BIDREQUEST_AMOUNT);

        Account account = accountMapper.selectByPrimaryKey(UserContext.getLoginInfo().getId());
        Assert.isFalse(bidRequest.getBidRequestAmount().compareTo(account.getRemainBorrowLimit())>0,
                "借款金额不能大于剩余授信额度");

        //借款利率
        Assert.isFalse(bidRequest.getCurrentRate().compareTo(SMALLEST_CURRENT_RATE)<0,
                "利率应大于"+SMALLEST_CURRENT_RATE);
        Assert.isFalse(bidRequest.getCurrentRate().compareTo(MAX_CURRENT_RATE)>0,
                "利率应小于"+MAX_CURRENT_RATE);

        //借款期限
        Assert.isFalse(!BORROW_RETURN_MONTHS.contains(bidRequest.getReturnMonthes().toString()),
                "借款期限不合法");

        //返款类型
        Assert.isFalse(bidRequest.getReturnType()!=RETURN_TYPE_MONTH_INTEREST_PRINCIPAL
        &&bidRequest.getReturnType()!=RETURN_TYPE_MONTH_INTEREST,
                "返款类型不合法");

        //最小投标金额、
        Assert.isFalse(bidRequest.getMinBidAmount().compareTo(SMALLEST_BID_AMOUNT)<0,
                "最小投标金额要大于"+SMALLEST_BID_AMOUNT);

        Assert.isFalse(bidRequest.getMinBidAmount().compareTo(bidRequest.getBidRequestAmount().multiply(new BigDecimal("0.5")))>0,
                "投标金额不得大于50%");

        //投标天数
        Assert.isFalse(!BID_DISABLE_DAYS.contains(bidRequest.getDisableDays().toString()),
                "投标天数不合法");

        //借款标题
        Assert.notNull(bidRequest.getTitle(),"借款标题不能为空");

        //借款描述
        Assert.notNull(bidRequest.getDescription(),"借款描述不能为空");

        //2.查看申请人是否处于可申请借款的状态
        UserInfo userInfo = userInfoService.getById(UserContext.getLoginInfo().getId());
        Assert.isFalse(userInfo.hasBidrequestInProcess(),"有借款在申请流程中");

        //3.是否满足借款条件
        Assert.isFalse(!userInfo.hasAllAuth(),"不满足借款条件");

        //4.保存借款对象信息

        BidRequest br = new BidRequest();
        br.setBidRequestAmount(bidRequest.getBidRequestAmount());
        br.setCurrentRate(bidRequest.getCurrentRate());
        br.setReturnMonthes(bidRequest.getReturnMonthes());
        br.setReturnType(bidRequest.getReturnType());
        br.setMinBidAmount(bidRequest.getMinBidAmount());
        br.setDisableDays(bidRequest.getDisableDays());
        br.setTitle(bidRequest.getTitle());
        br.setDescription(bidRequest.getDescription());

        //设置借款的状态,发标待审
        br.setBidRequestState(BIDREQUEST_STATE_APPLY);
        //创建人
        br.setCreateUser(UserContext.getLoginInfo());
        br.setApplyTime(new Date());
        br.setBidCount(0);
        br.setTotalRewardAmount(CalculatetUtil.calTotalInterest(br.getReturnType(),br.getBidRequestAmount(),br.getCurrentRate(),br.getReturnMonthes()));
        bidRequestMapper.insert(br);

        //更新userInfo对象信息
        userInfo.addBitState(BitStatesUtils.HAS_BIDREQUEST_IN_PROCESS);
        userInfoService.update(userInfo);
    }

    @Override
    public PageResult queryForPage(BidRequestQueryObject qo) {
        int count = bidRequestMapper.selectForCount(qo);
        if (count==0){
            return PageResult.empty(qo.getPageSize());
        }

        List listData = bidRequestMapper.selectForList(qo);

        return new PageResult(listData,count,qo.getCurrentPage(),qo.getPageSize());
    }

    @Override
    public void bidrequestAuth(Long id, Integer state, Date publishTime, String remark) {
        //参数校验

        //是否处于发标待审核状态
        BidRequest bidRequest = bidRequestMapper.selectByPrimaryKey(id);
        Assert.isFalse(bidRequest.getBidRequestState()!=BIDREQUEST_STATE_APPLY,
                "不处于发标待审核状态");


        //设置审核历史信息
        BidRequestAuditHistory brah = new BidRequestAuditHistory();
        brah.setRemark(remark);
        brah.setState(state);
        brah.setApplier(bidRequest.getCreateUser());
        brah.setApplyTime(bidRequest.getApplyTime());
        brah.setAuditor(UserContext.getLoginInfo());
        brah.setAuditTime(new Date());
        brah.setBidRequestId(id);
        brah.setAuditType(BidRequestAuditHistory.AUDITTYPE_PUBLISH);
        bidRequestAuditHistoryMapper.insert(brah);


        UserInfo userInfo = userInfoService.getById(bidRequest.getCreateUser().getId());
        //设置借钱申请对象信息
        if (state==BidRequestAuditHistory.STATE_SUCCESS){
            //审核成功
            if (publishTime!=null){
                //设置了发布时间
                bidRequest.setNote(remark);
                bidRequest.setPublishTime(publishTime);
                bidRequest.setBidRequestState(BIDREQUEST_STATE_PUBLISH_PENDING);
                bidRequest.setDisableDate(DateUtils.addDays(publishTime,bidRequest.getDisableDays()));
            }else {
                //没有设置发布时间
                bidRequest.setNote(remark);
                bidRequest.setPublishTime(new Date());
                bidRequest.setBidRequestState(BIDREQUEST_STATE_BIDDING);
                bidRequest.setDisableDate(DateUtils.addDays(new Date(),bidRequest.getDisableDays()));
            }
        }else {
            //审核失败
            bidRequest.setNote(remark);
            bidRequest.setBidRequestState(BIDREQUEST_STATE_PUBLISH_REFUSE);
            userInfo.setBitState(BitStatesUtils.removeState(userInfo.getBitState(),BitStatesUtils.HAS_BIDREQUEST_IN_PROCESS));
        }

        update(bidRequest);

        if (bidRequest.getBidRequestType()==BidRequest.BID_TYPE_CREDIT){
            userInfoService.update(userInfo);
        }

        if (state==BidRequestAuditHistory.STATE_SUCCESS&&publishTime!=null){
            //添加到待发布队列中
            bidRequest.setVersion(bidRequest.getVersion()+1);
            con.offer(bidRequest);
        }
    }

    @Override
    public List publishPendngBidRequests() {
        //待发标的5个标,按照发标时间先后排序，且有发标倒计时
        BidRequestQueryObject qo = new BidRequestQueryObject();
        qo.setBidRequestState(BIDREQUEST_STATE_PUBLISH_PENDING);
        qo.setPageSize(5);
        qo.setOrderByColumn("publish_time");
        qo.setOrderType("asc");
        List list = bidRequestMapper.selectForList(qo);
        return list;
    }

    @Override
    public void update(BidRequest bidRequest) {
        bidRequestMapper.updateByPrimaryKey(bidRequest);
    }

    @Override
    public List bidRequests() {
        //1，投标中；2，还款中；3已完成的标的5个标；注意，按照投标中>还款中>已完成的顺序排列；
        BidRequestQueryObject qo = new BidRequestQueryObject();
        qo.setBidRequestType(BidRequest.BID_TYPE_CREDIT);
        qo.setBidRequestStates(new int[]{1,7,8});
        qo.setPageSize(5);
        qo.setOrderByColumn("bid_request_state");
        qo.setOrderType("asc");
        List list = bidRequestMapper.selectForList(qo);
        return list;
    }

    @Override
    public BidRequest getById(Long id) {
        return bidRequestMapper.selectByPrimaryKey(id);
    }

    @Override
    public void audit1(Long id, Integer state, String remark) {
        //判断
        //1.判断借款对象状态要处于满标一审
        BidRequest bidRequest = bidRequestMapper.selectByPrimaryKey(id);
        Assert.isFalse(bidRequest.getBidRequestState()!=BIDREQUEST_STATE_APPROVE_PENDING_1,"标不处于满标一审状态");

        //2.设置审核相关信息(BidRequestAuditHistory)
        BidRequestAuditHistory brah = new BidRequestAuditHistory();
        brah.setRemark(remark);
        brah.setState(state);
        brah.setApplier(bidRequest.getCreateUser());
        brah.setApplyTime(bidRequest.getApplyTime());
        brah.setAuditor(UserContext.getLoginInfo());
        brah.setAuditTime(new Date());
        brah.setBidRequestId(id);
        brah.setAuditType(BidRequestAuditHistory.AUDITTYPE_AUDIT1);
        bidRequestAuditHistoryMapper.insert(brah);

        //3.如果审核成功：
        if (state==BidRequestAuditHistory.STATE_SUCCESS){
            //1.修改借款对象状态进入满标二审
            bidRequest.setBidRequestState(BIDREQUEST_STATE_APPROVE_PENDING_2);

            //2.这个借款下的所有投标记录的状态修改为满标二审
            bidService.updateStateByBidRequestId(id,BIDREQUEST_STATE_APPROVE_PENDING_2);

        }else {
            //4.如果审核失败：
            //1.修改借款对象状态为满标审核拒绝
            if (bidRequest.getBidRequestType()==BidRequest.BID_TYPE_CREDIT){
                //信用标
                auditRefuse(id, bidRequest);
            }else {
                //体验标
                auditRefuseExp(id, bidRequest);
            }
        }

        //更新标的状态
        update(bidRequest);


    }

    @Override
    public void audit2(Long id, Integer state, String remark) {
        //判断
        //1.判断借款对象状态要处于满标一审
        BidRequest bidRequest = bidRequestMapper.selectByPrimaryKey(id);
        Assert.isFalse(bidRequest.getBidRequestState()!=BIDREQUEST_STATE_APPROVE_PENDING_2,"标不处于满标2审状态");

        //2.设置审核相关信息(BidRequestAuditHistory)
        BidRequestAuditHistory brah = new BidRequestAuditHistory();
        brah.setRemark(remark);
        brah.setState(state);
        brah.setApplier(bidRequest.getCreateUser());
        brah.setApplyTime(bidRequest.getApplyTime());
        brah.setAuditor(UserContext.getLoginInfo());
        brah.setAuditTime(new Date());
        brah.setBidRequestId(id);
        brah.setAuditType(BidRequestAuditHistory.AUDITTYPE_AUDIT2);
        bidRequestAuditHistoryMapper.insert(brah);

        if (state==BidRequestAuditHistory.STATE_REJECT){
            //4.如果审核失败：
            auditRefuse(id, bidRequest);

        }else {
            //4.如果审核成功：

            //创建还款计划
            //创建收款计划
            paymentScheduleService.createPaymentSchedule(bidRequest);

            //投资人
            List<Bid> bids = bidRequest.getBids();
            Map<Long,Account> accounts = new HashMap<>();
            //1.冻结金额减少
            for (Bid b : bids){
                Long bidUserId = b.getBidUser().getId();
                Account bidAccount = accounts.get(bidUserId);
                if (bidAccount==null){
                    bidAccount = accountMapper.selectByPrimaryKey(bidUserId);
                    accounts.put(bidUserId,bidAccount);
                }
                bidAccount.setFreezedAmount(bidAccount.getFreezedAmount().subtract(b.getAvailableAmount()));
                //2.创建投标成功，解冻流水
                accountFlowService.createAccountFlow(AccountFlow.ACCOUNT_ACTIONTYPE_BID_SUCCESSFUL,
                        b.getAvailableAmount(),"["+bidRequest.getTitle()+"投标成功,冻结金额减少:"+b.getAvailableAmount(),
                        bidAccount);
                //3.投资人待收本金增加
                //bidAccount.setUnReceivePrincipal(bidAccount.getUnReceivePrincipal().add(b.getAvailableAmount()));

                //4.投资人待收利息增加
                //BigDecimal interest = bidRequest.getTotalRewardAmount().multiply(b.getAvailableAmount().divide(bidRequest.getBidRequestAmount(), SCALE_CAL, BigDecimal.ROUND_HALF_UP));
                //bidAccount.setUnReceiveInterest(bidAccount.getUnReceiveInterest().add(interest.setScale(SCALE_STORE,BigDecimal.ROUND_HALF_UP)));
            }

            //更新账户信息
            Collection<Account> values = accounts.values();
            for (Account value : values) {
                BigDecimal principal = paymentScheduleDetailService.selectUnReceivePrincipal(bidRequest.getId(), value.getId());
                BigDecimal interest = paymentScheduleDetailService.selectUnReceiveInterest(bidRequest.getId(), value.getId());
                value.setUnReceivePrincipal(value.getUnReceivePrincipal().add(principal));
                value.setUnReceiveInterest(value.getUnReceiveInterest().add(interest));
                accountService.update(value);
            }

            //借款人
            //1.可用余额增加收到借款
            Long loanUserId = bidRequest.getCreateUser().getId();
            Account loanAccount = accountService.getById(loanUserId);
            loanAccount.setUsableAmount(loanAccount.getUsableAmount().add(bidRequest.getBidRequestAmount()));
            //2.创建成功借款流水
            accountFlowService.createAccountFlow(AccountFlow.ACCOUNT_ACTIONTYPE_BIDREQUEST_SUCCESSFUL,
                    bidRequest.getBidRequestAmount(),
                    "["+bidRequest.getTitle()+"],标借款成功："+bidRequest.getBidRequestAmount(),
                    loanAccount);
            //3.信用额度减少
            loanAccount.setRemainBorrowLimit(loanAccount.getRemainBorrowLimit().subtract(bidRequest.getBidRequestAmount()));
            //4.待还本息增加
            loanAccount.setUnReturnAmount(loanAccount.getUnReturnAmount().add(bidRequest.getBidRequestAmount().add(bidRequest.getTotalRewardAmount())));
            //5.移除借款人时候有借款在申请流程中的位状态
            UserInfo loanUserInfo = userInfoService.getById(loanUserId);
            loanUserInfo.removeBitState(BitStatesUtils.HAS_BIDREQUEST_IN_PROCESS);
            //6.支付平台借款手续费 ： 可用余额减少
            BigDecimal serviceCharge = bidRequest.getBidRequestAmount().multiply(new BigDecimal("0.05"));
            loanAccount.setUsableAmount(loanAccount.getUsableAmount().subtract(serviceCharge));
            //7.创建支付借款手续费流水
            accountFlowService.createAccountFlow(AccountFlow.ACCOUNT_ACTIONTYPE_CHARGE,
                    serviceCharge,
                    "["+bidRequest.getTitle()+"],借款成功,支付平台手续费："+serviceCharge,
                    loanAccount);
            //平台账户
            //1.收取借款手续费：可用余额增加
            SystemAccount systemAccount = systemAccountService.getAccount();
            systemAccount.setUsableAmount(systemAccount.getUsableAmount().add(serviceCharge));
            systemAccountService.update(systemAccount);
            //2.创建平台账户收取借款手续费流水
            systemAccountFlowService.createFlow(SYSTEM_ACCOUNT_ACTIONTYPE_MANAGE_CHARGE,
                    serviceCharge,
                    "系统账户收到账户管理费（借款管理费）:"+serviceCharge,
                    systemAccount);



            //借款对象状态修改为还款中
            bidRequest.setBidRequestState(BIDREQUEST_STATE_PAYING_BACK);
            //这个借款下的投标对象修改为还款中
            bidService.updateStateByBidRequestId(bidRequest.getId(),BIDREQUEST_STATE_PAYING_BACK);

            //更新借款对象信息
            update(bidRequest);
            //更新借款人用户信息
            userInfoService.update(loanUserInfo);
            //更新借款人账户
            accountService.update(loanAccount);

        }
    }

    @Override
    public void publishExpBidRequest(BidRequest bidRequest) {
        LoginInfo loginInfo = UserContext.getLoginInfo();
        //参数校验:
        //大于最小发标金额
        Assert.isFalse(bidRequest.getBidRequestAmount().compareTo(Constants.SMALLEST_BIDREQUEST_AMOUNT)<0,"发标金额不能小于："+Constants.SMALLEST_BIDREQUEST_AMOUNT);
        //还款类型校验

        //保存发标
        BidRequest br = new BidRequest();
        br.setBidRequestType(BidRequest.BID_TYPE_EXP);
        br.setBidRequestState(Constants.BIDREQUEST_STATE_APPLY);
        br.setBidRequestAmount(bidRequest.getBidRequestAmount());
        br.setCurrentRate(bidRequest.getCurrentRate());
        br.setReturnMonthes(bidRequest.getReturnMonthes());
        br.setTitle(bidRequest.getTitle());
        br.setDescription("叩丁狼体验标");
        br.setCreateUser(loginInfo);
        br.setDisableDays(bidRequest.getDisableDays());
        br.setMinBidAmount(bidRequest.getMinBidAmount());
        br.setApplyTime(new Date());
        br.setReturnType(bidRequest.getReturnType());
        br.setTotalRewardAmount(CalculatetUtil.calTotalInterest(br.getReturnType(),br.getBidRequestAmount(),br.getCurrentRate(),br.getReturnMonthes()));

        //保存体验标
        bidRequestMapper.insert(br);

    }

    @Override
    public List expBidRequests() {
        //1，投标中；2，还款中；3已完成的标的5个标；注意，按照投标中>还款中>已完成的顺序排列；
        BidRequestQueryObject qo = new BidRequestQueryObject();
        qo.setBidRequestType(BidRequest.BID_TYPE_EXP);
        qo.setPageSize(5);
        List list = bidRequestMapper.selectForList(qo);
        return list;
    }

    @Override
    public void audit2Exp(Long id, Integer state, String remark) {
        //判断
        //1.判断借款对象状态要处于满标一审
        BidRequest bidRequest = bidRequestMapper.selectByPrimaryKey(id);
        Assert.isFalse(bidRequest.getBidRequestState()!=BIDREQUEST_STATE_APPROVE_PENDING_2,"标不处于满标2审状态");

        //2.设置审核相关信息(BidRequestAuditHistory)
        BidRequestAuditHistory brah = new BidRequestAuditHistory();
        brah.setRemark(remark);
        brah.setState(state);
        brah.setApplier(bidRequest.getCreateUser());
        brah.setApplyTime(bidRequest.getApplyTime());
        brah.setAuditor(UserContext.getLoginInfo());
        brah.setAuditTime(new Date());
        brah.setBidRequestId(id);
        brah.setAuditType(BidRequestAuditHistory.AUDITTYPE_AUDIT2);
        bidRequestAuditHistoryMapper.insert(brah);

        if (state==BidRequestAuditHistory.STATE_REJECT){
            //4.如果审核失败：
            auditRefuseExp(id, bidRequest);

        }else {
            //审核成功
            //创建还款计划
            //创建收款计划
            paymentScheduleService.createPaymentSchedule(bidRequest);

            //体验金投资人
            List<Bid> bids = bidRequest.getBids();
            Map<Long,ExpAccount> expAccountMap = new HashMap<>();
            for (Bid bid : bids) {
                Long bidUserId = bid.getBidUser().getId();
                ExpAccount expAccount = expAccountMap.get(bidUserId);
                if (expAccount==null){
                    expAccount = expAccountService.getById(bidUserId);
                    expAccountMap.put(bidUserId,expAccount);
                }
                //冻结金额减少
                expAccount.setFreezedAmount(expAccount.getFreezedAmount().subtract(bid.getAvailableAmount()));
                //冻结金额减少流水
                expAccountFlowService.createExpAccountFlow(Constants.EXPGOLD_FLOW_BIDSUCCESS_AMOUNT,
                        bid.getAvailableAmount(),
                        "["+bidRequest.getTitle()+"]，投标成功，冻结金额减少："+bid.getAvailableAmount(),
                        expAccount);

                //未还本金增加
                expAccount.setUnReturnExpAmount(expAccount.getUnReturnExpAmount().add(bid.getAvailableAmount()));
            }
            //保存账户信息
            Collection<ExpAccount> values = expAccountMap.values();
            for (ExpAccount value : values) {
                Account account = accountService.getById(value.getId());
                //利息
                BigDecimal interest = paymentScheduleDetailService.selectUnReceiveInterest(bidRequest.getId(), value.getId());
                account.setUnReceiveInterest(account.getUnReceiveInterest().add(interest));
                accountService.update(account);

                expAccountService.update(value);
            }

            //借款对象状态修改为还款中
            bidRequest.setBidRequestState(BIDREQUEST_STATE_PAYING_BACK);

            //这个借款下的投标对象修改为还款中
            bidService.updateStateByBidRequestId(bidRequest.getId(),BIDREQUEST_STATE_PAYING_BACK);

            //更新借款对象信息
            update(bidRequest);
        }
    }

    private void auditRefuse(Long id, BidRequest bidRequest) {
        //4.如果审核失败：
        //1.修改借款对象状态为满标审核拒绝
        bidRequest.setBidRequestState(BIDREQUEST_STATE_REJECTED);
        //2.这个借款下的所有投标记录的状态修改为满标审核拒绝
        bidService.updateStateByBidRequestId(id,BIDREQUEST_STATE_REJECTED);
        //3.退钱：这个借款的所有的投标人，冻结金额减少，可用余额增加
        List<Bid> bids = bidRequest.getBids();
        Map<Long,Account> accounts = new HashMap<>();
        for (Bid b : bids){
            Long bidUserId = b.getBidUser().getId();
            Account bidAccount = accounts.get(bidUserId);
            if (bidAccount==null){
                bidAccount = accountMapper.selectByPrimaryKey(bidUserId);
                accounts.put(bidUserId,bidAccount);
            }

            bidAccount.setFreezedAmount(bidAccount.getFreezedAmount().subtract(b.getAvailableAmount()));
            bidAccount.setUsableAmount(bidAccount.getUsableAmount().add(b.getAvailableAmount()));
            //4.创建投标失败，解冻流水
            accountFlowService.createAccountFlow(AccountFlow.ACCOUNT_ACTIONTYPE_BID_UNFREEZED,
                    b.getAvailableAmount(),"投标失败，解冻金额："+b.getAvailableAmount(),
                    bidAccount);
        }

        //更新账户信息
        Collection<Account> values = accounts.values();
        for (Account value : values) {
            accountService.update(value);
        }

        //5.借款人的时候有借款在申请流程中的位状态移除
        UserInfo userInfo = userInfoService.getById(bidRequest.getCreateUser().getId());
        userInfo.removeBitState(BitStatesUtils.HAS_BIDREQUEST_IN_PROCESS);
        userInfoService.update(userInfo);
    }

    private void auditRefuseExp(Long id, BidRequest bidRequest) {
        //4.如果审核失败：
        //1.修改借款对象状态为满标审核拒绝
        bidRequest.setBidRequestState(BIDREQUEST_STATE_REJECTED);
        //2.这个借款下的所有投标记录的状态修改为满标审核拒绝
        bidService.updateStateByBidRequestId(id,BIDREQUEST_STATE_REJECTED);
        //3.退钱：这个借款的所有的投标人，冻结金额减少，可用余额增加
        List<Bid> bids = bidRequest.getBids();
        Map<Long,ExpAccount> expAccountMap = new HashMap<>();
        for (Bid b : bids){
            Long bidUserId = b.getBidUser().getId();
            ExpAccount bidAccount = expAccountMap.get(bidUserId);
            if (bidAccount==null){
                bidAccount = expAccountService.getById(bidUserId);
                expAccountMap.put(bidUserId,bidAccount);
            }

            bidAccount.setFreezedAmount(bidAccount.getFreezedAmount().subtract(b.getAvailableAmount()));
            //4.创建投标失败，解冻流水
            expAccountFlowService.createExpAccountFlow(Constants.EXPGOLD_FLOW_REDUCE_FREE,
                    b.getAvailableAmount(),"["+bidRequest.getTitle()+"],投标失败，解冻金额："+b.getAvailableAmount(),
                    bidAccount);
            bidAccount.setUsableAmount(bidAccount.getUsableAmount().add(b.getAvailableAmount()));
            expAccountFlowService.createExpAccountFlow(Constants.EXPGOLD_FLOW_ADD_AMOUNT,
                    b.getAvailableAmount(),"["+bidRequest.getTitle()+"],投标失败，余额增加："+b.getAvailableAmount(),
                    bidAccount);
        }

        //更新账户信息
        Collection<ExpAccount> values = expAccountMap.values();
        for (ExpAccount value : values) {
            expAccountService.update(value);
        }
    }
}
