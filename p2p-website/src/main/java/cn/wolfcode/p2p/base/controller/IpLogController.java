package cn.wolfcode.p2p.base.controller;

import cn.wolfcode.p2p.base.anno.NeedLogin;
import cn.wolfcode.p2p.base.query.IpLogQueryObject;
import cn.wolfcode.p2p.base.service.IIpLogService;
import cn.wolfcode.p2p.util.PageResult;
import cn.wolfcode.p2p.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 前台日志
 */
@Controller
public class IpLogController {
    @Autowired
    private IIpLogService ipLogService;

    /**
     * 日志查询
     */
    @RequestMapping("/ipLog")
    @NeedLogin
    public String ipLog(@ModelAttribute("qo") IpLogQueryObject qo, Model model){
        qo.setPageSize(3);
        qo.setUsername(UserContext.getLoginInfo().getUsername());
        PageResult pageResult = ipLogService.queryForPage(qo);
        model.addAttribute("pageResult",pageResult);
        return "iplog_list";
    }
}
