package cn.wolfcode.p2p.listener;

import cn.wolfcode.p2p.business.domain.BidRequest;
import cn.wolfcode.p2p.business.mapper.BidRequestMapper;
import cn.wolfcode.p2p.business.query.BidRequestQueryObject;
import cn.wolfcode.p2p.business.service.impl.BidRequestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import static cn.wolfcode.p2p.util.Constants.BIDREQUEST_STATE_PUBLISH_PENDING;

/**
 * 服务启动时，加载待发布的借款到队列中
 */
@Component
public class PublishBidRequestListener implements ApplicationListener<ContextRefreshedEvent>{

    @Autowired
    private BidRequestMapper bidRequestMapper;



    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
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
}
