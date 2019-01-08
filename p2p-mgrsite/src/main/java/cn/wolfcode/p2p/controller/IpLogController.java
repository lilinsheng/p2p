package cn.wolfcode.p2p.controller;

import cn.wolfcode.p2p.base.query.IpLogQueryObject;
import cn.wolfcode.p2p.base.service.IIpLogService;
import cn.wolfcode.p2p.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 后台登录日志
 */
@Controller
public class IpLogController {

    @Autowired
    private IIpLogService ipLogService;

    /**
     * 日志列表
     */
    @RequestMapping("/ipLog")
    public String ipLog(@ModelAttribute("qo") IpLogQueryObject qo, Model model){
        PageResult pageResult = ipLogService.queryForPage(qo);
        model.addAttribute("pageResult",pageResult);
        return "ipLog/list";
    }
}
