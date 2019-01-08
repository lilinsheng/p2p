package cn.wolfcode.p2p.business;

import cn.wolfcode.p2p.base.anno.NeedLogin;
import cn.wolfcode.p2p.hosting.service.IHostingService;
import cn.wolfcode.p2p.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 托管
 */
@Controller
public class HostingController {
    @Autowired
    private IHostingService hostingService;

    /**
     * 托管账户个人中心
     */
    @RequestMapping("/sinaAccount")
    @NeedLogin
    public void sinaAccount(HttpServletResponse response) throws IOException {
        String address = hostingService.jumpAccount();
        response.sendRedirect(address);
    }

    /**
     * 支付密码
     */
    @RequestMapping("/payPassword")
    @NeedLogin
    public void payPassword(HttpServletResponse response) throws IOException {
        String url = hostingService.setPayPassword();
        response.sendRedirect(url);
    }

    /**
     * 设置支付密码回调地址
     */
    @RequestMapping("/return_payPassword")
    @ResponseBody
    public String returnPayPassword(Long mark){
        System.out.println(mark);
        return "设置支付密码成功";
    }

    /**
     * 绑定手机号
     */
    /*@RequestMapping("/bind_phone")
    @NeedLogin
    public JsonResult*/
}
