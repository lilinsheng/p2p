package cn.wolfcode.p2p.controller;

import cn.wolfcode.p2p.base.anno.NotNeedLogin;
import cn.wolfcode.p2p.base.domain.IpLog;
import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.base.exception.CustomException;
import cn.wolfcode.p2p.base.service.IIpLogService;
import cn.wolfcode.p2p.base.service.ILoginInfoService;
import cn.wolfcode.p2p.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 后台登录
 */
@Controller
public class LoginController {

    @Autowired
    private ILoginInfoService loginInfoService;

    @Autowired
    private IIpLogService ipLogService;


    /**
     * 登录
     */
    @RequestMapping("/login")
    @ResponseBody
    @NotNeedLogin
    public JsonResult login(LoginInfo loginInfo){
        loginInfo.setUserType(LoginInfo.USERTYPE_MGRSITE);
        int state = IpLog.STATE_ERROR;
        JsonResult jsonResult = new JsonResult();
        try {
            loginInfoService.login(loginInfo);
            state = IpLog.STATE_SUCCESS;
        }catch (CustomException e){
            jsonResult.setMsg(e.getMessage());
        }catch (Exception e){
            jsonResult.setMsg("系统异常，请联系客服");
        }

        ipLogService.save(loginInfo.getUsername(),loginInfo.getUserType(),state);
        return jsonResult;
    }
}
