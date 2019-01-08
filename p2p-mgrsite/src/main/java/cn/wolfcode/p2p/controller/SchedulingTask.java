package cn.wolfcode.p2p.controller;

import cn.wolfcode.p2p.business.domain.BidRequest;
import cn.wolfcode.p2p.business.domain.PaymentSchedule;
import cn.wolfcode.p2p.business.mapper.BidRequestMapper;
import cn.wolfcode.p2p.business.query.BidRequestQueryObject;
import cn.wolfcode.p2p.business.query.PaymentScheduleQueryObject;
import cn.wolfcode.p2p.business.service.IBidRequestService;
import cn.wolfcode.p2p.business.service.IExpAccountGrantRecordService;
import cn.wolfcode.p2p.business.service.IPaymentScheduleService;
import cn.wolfcode.p2p.business.service.impl.BidRequestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import static cn.wolfcode.p2p.util.Constants.*;

/**
 * 定时刷新待发布的借款对象
 */
@Component
public class SchedulingTask {

    @Autowired
    private BidRequestMapper bidRequestMapper;
    @Autowired
    private IBidRequestService bidRequestService;
    @Autowired
    private IPaymentScheduleService paymentScheduleService;
    @Autowired
    private IExpAccountGrantRecordService expAccountGrantRecordService;

    /**
     * 每10秒从数据库获取一次待发布的标
     */
    //@Scheduled(cron = "*/10 * * * * ?")
    public void publishPendingGTask() {
        BidRequestQueryObject qo = new BidRequestQueryObject();
        qo.setBidRequestState(BIDREQUEST_STATE_PUBLISH_PENDING);
        List<BidRequest> list = bidRequestMapper.selectForList(qo);
        for (BidRequest br : list) {
            if (!BidRequestServiceImpl.con.contains(br)){
                //当不包含当前借款对象，才添加到队列
                BidRequestServiceImpl.con.offer(br);
            }
        }
        System.out.println(BidRequestServiceImpl.con);
    }

    /**
     * 每秒从内存中读取待发布的标
     */
    @Scheduled(cron = "*/1 * * * * ?")
    public void publishTask() {
        Date date = new Date();
        ConcurrentLinkedQueue<BidRequest> con = BidRequestServiceImpl.con;
        for (BidRequest br : con) {
            //当发布时间在当前时间之前，更新标的状态为发布中
            if (br.getPublishTime().before(date)){
                br.setBidRequestState(BIDREQUEST_STATE_BIDDING);
                bidRequestService.update(br);
                con.remove(br);
            }
        }
    }

    /**
     * 滞纳金定时器
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void overdueAmount(){
        PaymentScheduleQueryObject qo = new PaymentScheduleQueryObject();
        qo.setState(PAYMENT_STATE_OVERDUE);
        qo.setPageSize(-1);
        List<PaymentSchedule> list = paymentScheduleService.queryForOverdue(qo);
        for (PaymentSchedule paymentSchedule : list) {
            BigDecimal overdue = paymentSchedule.getTotalAmount().add(paymentSchedule.getOverdueAmount()).multiply(new BigDecimal("0.008"));
                paymentSchedule.setOverdueAmount(paymentSchedule.getOverdueAmount().add(overdue));
            paymentScheduleService.update(paymentSchedule);
        }
    }

    /**
     * 体验金还款定时器：每晚0点检查还款时间是否已经到了
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void returnExpGold(){
        paymentScheduleService.returnExpGold();
    }

    /**
     * 体验金到期归还
     */
    @Scheduled(cron = "0 10 0 * * ?")
    public void returnSystemExpGold(){
        expAccountGrantRecordService.returnSystemExpGold();
    }
}
