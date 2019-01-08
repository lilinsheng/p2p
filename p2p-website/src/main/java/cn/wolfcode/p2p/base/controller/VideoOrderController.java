package cn.wolfcode.p2p.base.controller;

import cn.wolfcode.p2p.base.anno.NeedLogin;
import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.base.domain.OrderTime;
import cn.wolfcode.p2p.base.domain.UserInfo;
import cn.wolfcode.p2p.base.domain.VideoAuth;
import cn.wolfcode.p2p.base.service.ILoginInfoService;
import cn.wolfcode.p2p.base.service.IOrderTimeService;
import cn.wolfcode.p2p.base.service.IUserInfoService;
import cn.wolfcode.p2p.base.service.IVideoAuthService;
import cn.wolfcode.p2p.util.JsonResult;
import cn.wolfcode.p2p.util.UserContext;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 前台视频认证
 */
@Controller
public class VideoOrderController {

    @Autowired
    private ILoginInfoService loginInfoService;
    @Autowired
    private IOrderTimeService orderTimeService;
    @Autowired
    private IVideoAuthService videoAuthService;
    @Autowired
    private IUserInfoService userInfoService;

    /**
     * 视频认证预约
     */
    @NeedLogin
    @RequestMapping("/videoAuditOrder")
    public String videoAuditOrder(Model model){
        LoginInfo loginInfo = UserContext.getLoginInfo();
        UserInfo userInfo = userInfoService.getById(loginInfo.getId());
        if (userInfo.hasVedioAuth()){
            //当前用户已经完成视频认证
            model.addAttribute("videoSuccess",true);
        }else if (userInfo.getVideoAuthId()!=null){
            //当前用户已经预约了视频认证
            VideoAuth videoAuditOrder = videoAuthService.getById(userInfo.getVideoAuthId());
            model.addAttribute("videoAuditOrder",videoAuditOrder);
        }else {
            //预约客服
            List<LoginInfo> auditors = loginInfoService.getAuditors();
            model.addAttribute("auditors",auditors);

            //预约日期
            List<Date> orderDates = new ArrayList<>();
            Date date = new Date();
            orderDates.add(DateUtils.addDays(date,1));
            orderDates.add(DateUtils.addDays(date,2));
            orderDates.add(DateUtils.addDays(date,3));
            model.addAttribute("orderDates",orderDates);

            //预约时间段
            List<OrderTime> orderTimes = orderTimeService.listAll();
            model.addAttribute("orderTimes",orderTimes);
        }

        return "videoOrder";
    }

    /**
     * 预约客服
     */
    @NeedLogin
    @RequestMapping("/saveVideoAuditOrder")
    @ResponseBody
    public JsonResult videoAuditOrder(VideoAuth videoAuth){
        videoAuthService.apply(videoAuth);
        return JsonResult.instance();
    }
}
