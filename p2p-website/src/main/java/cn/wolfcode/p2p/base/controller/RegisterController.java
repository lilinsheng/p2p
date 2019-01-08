package cn.wolfcode.p2p.base.controller;

import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.base.service.ILoginInfoService;
import cn.wolfcode.p2p.base.service.ISendVerifyCodeService;
import cn.wolfcode.p2p.base.vo.LoginInfoVO;
import cn.wolfcode.p2p.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
public class RegisterController {
    @Autowired
    private ILoginInfoService loginInfoService;
    @Autowired
    private ISendVerifyCodeService sendVerifyCodeService;

    @RequestMapping("/userRegister")
    @ResponseBody
    public JsonResult userRegister(LoginInfoVO loginInfoVO){
            loginInfoService.userRegister(loginInfoVO);
        return JsonResult.instance();
    }

    @RequestMapping("/list")
    @ResponseBody
    public LoginInfo get(){
        return loginInfoService.getById(1L);
    }

    @RequestMapping("/existUsername")
    @ResponseBody
    public boolean existUsername(String username){
        return !loginInfoService.existUsername(username);
    }

    /**
     * 发送验证码
     */
    @RequestMapping("/sendVerifyCode")
    @ResponseBody
    public JsonResult sendVerifyCode(String phone){
        sendVerifyCodeService.send(phone);
        return JsonResult.instance();
    }
}
