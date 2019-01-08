package cn.wolfcode.p2p.controller;

import cn.wolfcode.p2p.business.domain.PlatformBankInfo;
import cn.wolfcode.p2p.business.query.RechargeOfflineQueryObject;
import cn.wolfcode.p2p.business.service.IPlatformBankInfoService;
import cn.wolfcode.p2p.business.service.IRechargeOfflineService;
import cn.wolfcode.p2p.util.JsonResult;
import cn.wolfcode.p2p.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 *后台线下充值
 */
@Controller
public class RechargeOfflineController {

    @Autowired
    private IRechargeOfflineService rechargeOfflineService;

    @Autowired
    private IPlatformBankInfoService platformBankInfoService;

    /**
     * 线下充值审核列表
     */
    @RequestMapping("/rechargeOffline")
    public String rechargeOffline(@ModelAttribute("qo") RechargeOfflineQueryObject qo, Model model){
        PageResult pageResult = rechargeOfflineService.queryForPage(qo);
        model.addAttribute("pageResult",pageResult);

        //银行账户信息
        List<PlatformBankInfo> banks = platformBankInfoService.listAll();
        model.addAttribute("banks",banks);
        return "rechargeoffline/list";
    }

    /**
     * 线下充值审核
     */
    @RequestMapping("/rechargeOffline_audit")
    @ResponseBody
    public JsonResult rechargeOfflineAudit(Long id,Integer state,String remark){
        rechargeOfflineService.audit(id,state,remark);
        return JsonResult.instance();
    }
}
