package cn.wolfcode.p2p.controller;

import cn.wolfcode.p2p.base.query.VideoAuthQueryObject;
import cn.wolfcode.p2p.base.service.ILoginInfoService;
import cn.wolfcode.p2p.base.service.IVideoAuthService;
import cn.wolfcode.p2p.util.JsonResult;
import cn.wolfcode.p2p.util.PageResult;
import cn.wolfcode.p2p.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 后台视频认证
 */
@Controller
public class VideoAuthController {

    @Autowired
    private IVideoAuthService videoAuthService;
    @Autowired
    private ILoginInfoService loginInfoService;

    /**
     * 视频认证列表
     */
    @RequestMapping("/videoAuth")
    public String videoAuth(@ModelAttribute("qo")VideoAuthQueryObject qo, Model model){
        qo.setAuditorId(UserContext.getLoginInfo().getId());
        PageResult pageResult = videoAuthService.queryForPage(qo);
        model.addAttribute("pageResult",pageResult);
        return "videoAuth/list";
    }

    /**
     * 视频认证
     */
    @RequestMapping("/vedioAuth_audit")
    @ResponseBody
    public JsonResult vedioAuthAudit(Long id,Integer state,String remark){
        videoAuthService.videoAuth(id,state,remark);
        return JsonResult.instance();
    }
}
