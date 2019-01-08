package cn.wolfcode.p2p.controller;

import cn.wolfcode.p2p.business.domain.BidRequest;
import cn.wolfcode.p2p.business.query.BidRequestQueryObject;
import cn.wolfcode.p2p.business.service.IBidRequestService;
import cn.wolfcode.p2p.util.JsonResult;
import cn.wolfcode.p2p.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

import static cn.wolfcode.p2p.util.Constants.*;

/**
 * 借款申请后台
 */
@Controller
public class BorrowController {

    @Autowired
    private IBidRequestService bidRequestService;

    /**
     * 借款申请审核列表
     */
    @RequestMapping("/bidrequest_publishaudit_list")
    public String bidrequestPublishauditList(@ModelAttribute("qo")BidRequestQueryObject qo, Model model){
        qo.setBidRequestState(BIDREQUEST_STATE_APPLY);
        PageResult pageResult = bidRequestService.queryForPage(qo);
        model.addAttribute("pageResult",pageResult);
        return "bidrequest/publish_audit";
    }

    /**
     * 发标前审核
     */
    @RequestMapping("/bidrequest_publishaudit")
    @ResponseBody
    public JsonResult bidrequestPublishaudit(Long id, Integer state, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date publishTime,String remark){
        bidRequestService.bidrequestAuth(id,state,publishTime,remark);
        return JsonResult.instance();
    }
    /**
     * 满标一审列表
     */
    @RequestMapping("/bidrequest_audit1_list")
    public String bidrequestAudit1List(@ModelAttribute("qo")BidRequestQueryObject qo, Model model){
        qo.setBidRequestState(BIDREQUEST_STATE_APPROVE_PENDING_1);
        PageResult pageResult = bidRequestService.queryForPage(qo);
        model.addAttribute("pageResult",pageResult);
        return "bidrequest/audit1";
    }

    /**
     * 满标一审审核
     */
    @RequestMapping("/bidrequest_audit1")
    @ResponseBody
    public JsonResult bidrequestAudit1(Long id, Integer state,String remark){
        bidRequestService.audit1(id,state,remark);
        return JsonResult.instance();
    }
    /**
     * 满标2审列表
     */
    @RequestMapping("/bidrequest_audit2_list")
    public String bidrequestAudit2List(@ModelAttribute("qo")BidRequestQueryObject qo, Model model){
        qo.setBidRequestState(BIDREQUEST_STATE_APPROVE_PENDING_2);
        PageResult pageResult = bidRequestService.queryForPage(qo);
        model.addAttribute("pageResult",pageResult);
        return "bidrequest/audit2";
    }

    /**
     * 满标2审审核
     */
    @RequestMapping("/bidrequest_audit2")
    @ResponseBody
    public JsonResult bidrequestAudit2(Long id, Integer state,String remark){
        BidRequest bidRequest = bidRequestService.getById(id);
        if (bidRequest.getBidRequestType()==BidRequest.BID_TYPE_CREDIT){
            //信用标
            bidRequestService.audit2(id,state,remark);
        }else {
            //体验标
            bidRequestService.audit2Exp(id,state,remark);
        }
        return JsonResult.instance();
    }
}
