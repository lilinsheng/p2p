package cn.wolfcode.p2p.business;

import cn.wolfcode.p2p.business.service.IBidRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * p2p首页
 */
@Controller
public class IndexController {

    @Autowired
    private IBidRequestService bidRequestService;

    /**
     * 进入首页
     */
    @RequestMapping("/index")
    public String index(Model model){
        //首页待发布借款
        List publishPendngBidRequests = bidRequestService.publishPendngBidRequests();
        model.addAttribute("publishPendngBidRequests",publishPendngBidRequests);

        //进行中的借款
        List bidRequests = bidRequestService.bidRequests();
        model.addAttribute("bidRequests",bidRequests);

        //进行中的体验标
        List expBidRequests = bidRequestService.expBidRequests();
        model.addAttribute("expBidRequests",expBidRequests);
        return "main";
    }
}
