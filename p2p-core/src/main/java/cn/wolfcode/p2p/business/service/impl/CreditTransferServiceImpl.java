package cn.wolfcode.p2p.business.service.impl;
import java.util.Date;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.business.domain.AccountFlow;
import cn.wolfcode.p2p.business.domain.BidRequest;
import cn.wolfcode.p2p.business.domain.CreditTransfer;
import cn.wolfcode.p2p.business.domain.PaymentScheduleDetail;
import cn.wolfcode.p2p.business.mapper.CreditTransferMapper;
import cn.wolfcode.p2p.business.query.TransferQueryObject;
import cn.wolfcode.p2p.business.service.IAccountFlowService;
import cn.wolfcode.p2p.business.service.IBidRequestService;
import cn.wolfcode.p2p.business.service.ICreditTransferService;
import cn.wolfcode.p2p.business.service.IPaymentScheduleDetailService;
import cn.wolfcode.p2p.util.Assert;
import cn.wolfcode.p2p.util.Constants;
import cn.wolfcode.p2p.util.PageResult;
import cn.wolfcode.p2p.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CreditTransferServiceImpl implements ICreditTransferService {
    @Autowired
    private CreditTransferMapper creditTransferMapper;
    @Autowired
    private IPaymentScheduleDetailService paymentScheduleDetailService;
    @Autowired
    private IBidRequestService bidRequestService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IAccountFlowService accountFlowService;

    @Override
    public PageResult queryForCanTransPage(TransferQueryObject qo) {
        int count = creditTransferMapper.selectForCanTransCount(qo);
        if (count==0){
            return PageResult.empty(qo.getPageSize());
        }

        List listData = creditTransferMapper.selectForCanTransList(qo);

        return new PageResult(listData,count,qo.getCurrentPage(),qo.getPageSize());
    }

    @Override
    public void creditTransfer(Long[] bidIds) {
        LoginInfo loginInfo = UserContext.getLoginInfo();
        //查询转让的投资
        List<CreditTransfer> list = creditTransferMapper.selectForTransList(bidIds, loginInfo.getId());
        for (CreditTransfer ct : list){
            ct.setBidRequestState(Constants.TRANSFER_STATE_TRANSING);
            ct.setTransFrom(loginInfo);
            ct.setPublishDate(new Date());
            creditTransferMapper.insert(ct);

            //更新收款记录中债权转让状态
            paymentScheduleDetailService.batchTransFerStateByBidIdAndToLoginInfoId(ct.getBidId(),loginInfo.getId(), PaymentScheduleDetail.TRANSFER_STATE_TRANS);
        }
    }

    @Override
    public PageResult queryForPage(TransferQueryObject qo) {
        int count = creditTransferMapper.selectForCount(qo);
        if (count==0){
            return PageResult.empty(qo.getPageSize());
        }

        List listData = creditTransferMapper.selectForList(qo);

        return new PageResult(listData,count,qo.getCurrentPage(),qo.getPageSize());
    }

    @Override
    public void subscribe(Long id) {
        LoginInfo loginInfo = UserContext.getLoginInfo();
        CreditTransfer ct = creditTransferMapper.selectByPrimaryKey(id);
        //判断标的状态：招标中
        Assert.isFalse(ct.getBidRequestState()!=Constants.TRANSFER_STATE_TRANSING,"债权标不处于招标中");

        //判断认购人不能为转让人
        Assert.isFalse(loginInfo.getId()==ct.getTransFrom().getId(),"不能购买自己转让的债权标");

        //判断认购人不能是借款人
        BidRequest bidRequest = bidRequestService.getById(ct.getBidRequestId());
        LoginInfo borrowUser = bidRequest.getCreateUser();
        Assert.isFalse(loginInfo.getId()==borrowUser.getId(),"认购人不能是借款人");

        //判断认购人的余额是否足够
        Account transToAccount = accountService.getById(loginInfo.getId());
        Assert.isFalse(transToAccount.getUsableAmount().compareTo(ct.getBidRequestAmount())<0,"余额不足购买债券标");

        //---------------认购人-------------
        //可用余额减少
        transToAccount.setUsableAmount(transToAccount.getUsableAmount().subtract(ct.getBidRequestAmount()));
        //生成认购成功债权标的流水
        accountFlowService.createAccountFlow(AccountFlow.ACCOUNT_ACTIONTYPE_BUY_TRANSBID,
                ct.getBidRequestAmount(),
                "购买债权标["+ct.getBidRequestTitle()+"]成功，可用余额减少："+ct.getBidRequestAmount(),
                transToAccount);
        //待收本金增加
        transToAccount.setUnReceivePrincipal(transToAccount.getUnReceivePrincipal().add(ct.getBidRequestAmount()));
        //待售利息增加
        transToAccount.setUnReceiveInterest(transToAccount.getUnReceiveInterest().add(ct.getRemainInterest()));
        accountService.update(transToAccount);

        //----------------转让人---------------
        Account transFromAccount = accountService.getById(ct.getTransFrom().getId());
        //可用余额增加
        transFromAccount.setUsableAmount(transFromAccount.getUsableAmount().add(ct.getBidRequestAmount()));
        //生成转让债权流水
        accountFlowService.createAccountFlow(AccountFlow.ACCOUNT_ACTIONTYPE_SELL_TRANSBID,
                ct.getBidRequestAmount(),
                "转让债权标["+ct.getBidRequestTitle()+"]成功，可用余额增加："+ct.getBidRequestAmount(),
                transFromAccount);
        //待收本金减少
        transFromAccount.setUnReceivePrincipal(transFromAccount.getUnReceivePrincipal().subtract(ct.getBidRequestAmount()));
        //待收利息减少
        transFromAccount.setUnReceiveInterest(transFromAccount.getUnReceiveInterest().subtract(ct.getRemainInterest()));
        accountService.update(transFromAccount);

        //修改债权标对应收款计划的债权状态为正常
        paymentScheduleDetailService.batchTransFerStateByBidIdAndToLoginInfoId(ct.getBidId(),ct.getTransFrom().getId(),PaymentScheduleDetail.TRANSFER_STATE_NORMAL);
        //修改债权标对应收款计划的收款人为认购人
        paymentScheduleDetailService.batchUpdateToLoginInfoBy(ct.getBidId(),loginInfo.getId());
        //修改转让记录的状态为已转让
        ct.setBidRequestState(Constants.TRANSFER_STATE_TRANSFERRED);
        //设置认购人
        ct.setTransTo(loginInfo);
        //设置认购时间
        ct.setTransDate(new Date());
        update(ct);
    }

    @Override
    public void update(CreditTransfer ct) {
        Assert.isFalse(0==creditTransferMapper.updateByPrimaryKey(ct),"更新债券标转让失败[乐观锁]");
    }

    @Override
    public void batchUpdateTransStateInTransingByBidRequestId(Long bidRequestId) {
        creditTransferMapper.batchUpdateTransStateInTransingByBidRequestId(bidRequestId);
    }

    @Override
    public PageResult queryForMyTransPage(TransferQueryObject qo) {
        int count = creditTransferMapper.selectForMyTransCount(qo);
        if (count==0){
            return PageResult.empty(qo.getPageSize());
        }

        List listData = creditTransferMapper.selectForMyTransList(qo);

        return new PageResult(listData,count,qo.getCurrentPage(),qo.getPageSize());
    }

    @Override
    public void cancelTrans(Long bidId) {
        //判断债权标处于转让中
        CreditTransfer ct = creditTransferMapper.selectByPrimaryKey(bidId);
        Assert.isFalse(ct.getBidRequestState()!=Constants.TRANSFER_STATE_TRANSING,"债权标不处于转让中");

        //判断当前用户为债权标的转让人
        LoginInfo transFromUser = UserContext.getLoginInfo();
        Assert.isFalse(transFromUser.getId()!=ct.getTransFrom().getId(),"不能撤消别人的债权标");

        //修改债权标对应的收款记录的转让状态
        paymentScheduleDetailService.batchTransFerStateByBidIdAndToLoginInfoId(ct.getBidId(),transFromUser.getId(),PaymentScheduleDetail.TRANSFER_STATE_NORMAL);

        //修改债权标的状态为已撤销
        ct.setBidRequestState(Constants.TRANSFER_STATE_UNDO);
        update(ct);
    }
}
