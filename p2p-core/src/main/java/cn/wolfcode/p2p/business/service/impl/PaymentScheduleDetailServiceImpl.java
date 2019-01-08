package cn.wolfcode.p2p.business.service.impl;
import java.math.BigDecimal;
import java.util.Date;
import cn.wolfcode.p2p.base.domain.LoginInfo;

import cn.wolfcode.p2p.business.domain.Bid;
import cn.wolfcode.p2p.business.domain.BidRequest;
import cn.wolfcode.p2p.business.domain.PaymentSchedule;
import cn.wolfcode.p2p.business.domain.PaymentScheduleDetail;
import cn.wolfcode.p2p.business.mapper.PaymentScheduleDetailMapper;
import cn.wolfcode.p2p.business.service.IBidService;
import cn.wolfcode.p2p.business.service.IPaymentScheduleDetailService;
import cn.wolfcode.p2p.util.CalculatetUtil;
import cn.wolfcode.p2p.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PaymentScheduleDetailServiceImpl implements IPaymentScheduleDetailService {
    @Autowired
    private PaymentScheduleDetailMapper paymentScheduleDetailMapper;
    @Autowired
    private IBidService bidService;

    @Override
    public void createPaymentScheduleDetail(PaymentSchedule ps, BidRequest bidRequest) {
        //所有投资对象
        List<Bid> bids = bidRequest.getBids();
        BigDecimal totalPrincipal = Constants.ZERO;
        BigDecimal totalInterest = Constants.ZERO;
        BigDecimal principal;
        BigDecimal interest;
        for (int i = 0;i<bids.size();i++){
            Bid b = bids.get(i);
            PaymentScheduleDetail detail = new PaymentScheduleDetail();

            if (i==bids.size()-1){
                principal = ps.getPrincipal().subtract(totalPrincipal);
                interest = ps.getInterest().subtract(totalInterest);
            }else {
                //计算收款期本金 :当前bid投标金额 / 借款总额 * 当前期还款本金
                principal = CalculatetUtil.calMonthlyReceivePrincipal(b.getAvailableAmount(), bidRequest.getBidRequestAmount(), ps.getPrincipal());
                totalPrincipal = totalPrincipal.add(principal);
                //利息
                interest = CalculatetUtil.calMonthlyReceiveInterest(b.getAvailableAmount() ,bidRequest.getBidRequestAmount(),ps.getInterest());
                totalInterest = totalInterest.add(interest);
            }

            detail.setPrincipal(principal);
            detail.setInterest(interest);
            //收款总额
            detail.setTotalAmount(principal.add(interest));
            //查询该投资人对该标的总投资
            BigDecimal bidAmount = bidService.getBidAmountByBidRequestIdAndUserId(b.getBidRequestId(),b.getBidUser().getId());
            detail.setBidAmount(bidAmount);
            detail.setBidId(b.getId());
            detail.setMonthIndex(ps.getMonthIndex());
            detail.setDeadLine(ps.getDeadLine());
            detail.setBidRequestId(ps.getBidRequestId());
            detail.setReturnType(ps.getReturnType());
            detail.setPaymentScheduleId(ps.getId());
            detail.setFromLoginInfo(ps.getBorrowUser());
            detail.setToLoginInfoId(b.getBidUser().getId());

            //保存收款计划
            paymentScheduleDetailMapper.insert(detail);
        }
    }

    @Override
    public List<PaymentScheduleDetail> getDetailsByPsIdAndBidRequestId(Long psId, Long bidRequestId) {
        return paymentScheduleDetailMapper.selectDetailsByPsIdAndBidRequestId(psId,bidRequestId);
    }

    @Override
    public void update(PaymentScheduleDetail detail) {
        paymentScheduleDetailMapper.updateByPrimaryKey(detail);
    }

    @Override
    public BigDecimal selectUnReceivePrincipal(Long bidRequestId, Long bidUserId) {
        return paymentScheduleDetailMapper.selectUnReceivePrincipal(bidRequestId,bidUserId);
    }

    @Override
    public BigDecimal selectUnReceiveInterest(Long bidRequestId, Long bidUserId) {
        return paymentScheduleDetailMapper.selectUnReceiveInterest(bidRequestId,bidUserId);
    }

    @Override
    public void batchTransFerStateByBidIdAndToLoginInfoId(Long bidId, Long toLoginInfoId, Integer transFerState) {
        paymentScheduleDetailMapper.batchTransFerStateByBidIdAndToLoginInfoId(bidId,toLoginInfoId,transFerState);
    }

    @Override
    public void batchUpdateToLoginInfoBy(Long bidId, Long toLoginInfoId) {
        paymentScheduleDetailMapper.batchUpdateToLoginInfoBy(bidId,toLoginInfoId);
    }

    @Override
    public void batchTransFerStateByBidRequestId(Long bidRequestId) {
        paymentScheduleDetailMapper.batchTransFerStateByBidRequestId(bidRequestId);
    }

    @Override
    public BigDecimal getPrincipalByBidId(Long bidId) {
        return paymentScheduleDetailMapper.selectrincipalByBidId(bidId);
    }
}
