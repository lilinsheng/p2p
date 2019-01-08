package cn.wolfcode.p2p.controller;

import cn.wolfcode.p2p.base.query.RealAuthQueryObject;
import cn.wolfcode.p2p.base.service.IRealAuthService;
import cn.wolfcode.p2p.util.JsonResult;
import cn.wolfcode.p2p.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 后台实名审核
 */
@Controller
public class RealAuthController {

    @Autowired
    private IRealAuthService realAuthService;

    /**
     * 实名审核列表
     */
    @RequestMapping("/realAuth")
    public String realAuth(@ModelAttribute("qo") RealAuthQueryObject qo, Model model){
        PageResult pageResult = realAuthService.queryForPage(qo);
        model.addAttribute("pageResult",pageResult);
        return "realAuth/list";
    }

    /**
     * 后台通过审核，拒绝审核
     */
    @RequestMapping("/realAuth_audit")
    @ResponseBody
    public JsonResult realAuthAudit(Long realAuthId,Integer state,String remark){
        realAuthService.realAuthAudit(realAuthId,state,remark);
        return JsonResult.instance();
    }
}
