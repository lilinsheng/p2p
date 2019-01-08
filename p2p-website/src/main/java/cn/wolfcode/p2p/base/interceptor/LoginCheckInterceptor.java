package cn.wolfcode.p2p.base.interceptor;

import cn.wolfcode.p2p.base.anno.NeedLogin;
import cn.wolfcode.p2p.util.UserContext;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录信息拦截器
 */
@Component
public class LoginCheckInterceptor extends HandlerInterceptorAdapter{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod){

            //获取当前的方法对象
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            //判断是否存在NeedLogin注解
            boolean isNeedLogin = handlerMethod.hasMethodAnnotation(NeedLogin.class);

            //贴了注解且没有登录
            if (isNeedLogin&&UserContext.getLoginInfo()==null){
                response.sendRedirect("/login.html");
                return false;
            }
        }


        //放行
        return true;
    }
}
