package cn.wolfcode.p2p.util;

import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.base.vo.VerifyCodeVO;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class UserContext {


    private static final String VERIFYCODE_IN_SESSION = "verifycode_in_session";
    private static final String LoginINFO_IN_SESSION = "loginInfo";

    private static HttpSession getHttpSession() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return servletRequestAttributes.getRequest().getSession();
    }

    public static void setVerifyCodeVO(VerifyCodeVO vo){
        HttpSession session = getHttpSession();
        session.setAttribute(VERIFYCODE_IN_SESSION,vo);
    }

    public static VerifyCodeVO getVerifyCodeVO(){
        return (VerifyCodeVO) getHttpSession().getAttribute(VERIFYCODE_IN_SESSION);
    }

    public static void setLoginInfo(LoginInfo loginInfo){
        getHttpSession().setAttribute(LoginINFO_IN_SESSION,loginInfo);
    }

    public static LoginInfo getLoginInfo(){
        return (LoginInfo) getHttpSession().getAttribute(LoginINFO_IN_SESSION);
    }

    public static String getIP(){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return servletRequestAttributes.getRequest().getRemoteAddr();
    }

}
