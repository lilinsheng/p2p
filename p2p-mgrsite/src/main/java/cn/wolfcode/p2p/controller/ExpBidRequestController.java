package cn.wolfcode.p2p.controller;

import cn.wolfcode.p2p.business.domain.BidRequest;
import cn.wolfcode.p2p.business.query.BidRequestQueryObject;
import cn.wolfcode.p2p.business.service.IBidRequestService;
import cn.wolfcode.p2p.util.Constants;
import cn.wolfcode.p2p.util.JsonResult;
import cn.wolfcode.p2p.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 体验标
 */
@Controller
public class ExpBidRequestController {

    @Autowired
    private IBidRequestService bidRequestService;

    /**
     * 进入体验标的发布
     */
    @RequestMapping("/intoPublishExp")
    public String expBidRequestPublish(Model model){
        model.addAttribute("minBidRequestAmount", Constants.SMALLEST_BIDREQUEST_AMOUNT);
        model.addAttribute("minBidAmount", Constants.SMALLEST_BID_AMOUNT);
        return "expbidrequest/expbidrequestpublish";
    }

    /**
     * 发布体验标
     */
    @RequestMapping("/expBidRequestPublish")
    @ResponseBody
    public JsonResult publishexpBidRequest(BidRequest bidRequest){
        bidRequestService.publishExpBidRequest(bidRequest);
        return JsonResult.instance();
    }

    /**
     * 体验标列表
     */
    @RequestMapping("/expBidRequest_list")
    public String expbidrequestList(@ModelAttribute("qo")BidRequestQueryObject qo,Model model){
        qo.setBidRequestType(BidRequest.BID_TYPE_EXP);
        PageResult pageResult = bidRequestService.queryForPage(qo);
        model.addAttribute("pageResult",pageResult);
        return "expbidrequest/expbidrequestlist";
    }
}
