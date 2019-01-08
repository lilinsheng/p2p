package cn.wolfcode.p2p.business;

import cn.wolfcode.p2p.base.anno.NeedLogin;
import cn.wolfcode.p2p.base.query.QueryObject;
import cn.wolfcode.p2p.business.domain.RechargeOffline;
import cn.wolfcode.p2p.business.domain.RechargeOnline;
import cn.wolfcode.p2p.business.service.IPlatformBankInfoService;
import cn.wolfcode.p2p.business.service.IRechargeOfflineService;
import cn.wolfcode.p2p.business.service.IRechargeOnlineService;
import cn.wolfcode.p2p.hosting.service.IHostingService;
import cn.wolfcode.p2p.util.JsonResult;
import cn.wolfcode.p2p.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * 线下充值
 */
@Controller
public class RechargeController {

    @Autowired
    private IPlatformBankInfoService platformBankInfoService;

    @Autowired
    private IRechargeOfflineService rechargeOfflineService;
    @Autowired
    private IHostingService hostingService;
    @Autowired
    private IRechargeOnlineService rechargeOnlineService;

    /**
     * 进入充值页面
     */
    @RequestMapping("/recharge")
    @NeedLogin
    public String recharge(Model model){
        //平台银行列表
        QueryObject qo = new QueryObject();
        qo.setPageSize(-1);
        PageResult pageResult = platformBankInfoService.queryForPage(qo);
        model.addAttribute("banks",pageResult.getListData());
        return "recharge";
    }

    /**
     * 线下充值申请
     */
    @RequestMapping("/recharge_save")
    @NeedLogin
    @ResponseBody
    public JsonResult rechargeSave(RechargeOffline rechargeOffline){
        rechargeOfflineService.recharge(rechargeOffline);
        return JsonResult.instance();
    }

    /**
     * 线上充值
     */
    @RequestMapping("/rechargeOnline")
    @NeedLogin
    public void rechargeOnline(BigDecimal amount, HttpServletResponse response) throws IOException {
        String html = hostingService.recharge(amount);
        response.getWriter().write(html);
    }

    /**
     * 页面跳转同步返回页面路径
     */
    @RequestMapping("/returnSuccess")
    public String returnSuccess(String tradeCode,Model model){
        RechargeOnline ro = rechargeOnlineService.getByTradeCode(tradeCode);
        model.addAttribute("ro",ro);
        return "recharge_result";
    }

    /**
     * 系统异步回调通知地址
     */
    @RequestMapping("/notifySuccess")
    @ResponseBody
    public String notifySuccess(HttpServletRequest request){
        return rechargeOnlineService.notifySuccess(request);
    }
}
