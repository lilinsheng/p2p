package cn.wolfcode.p2p.listener;

import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.base.mapper.LoginInfoMapper;
import cn.wolfcode.p2p.base.service.ILoginInfoService;
import cn.wolfcode.p2p.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 后台应用首次启动时，创建管理员账户
 */
@Component
public class LoginListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private LoginInfoMapper loginInfoMapper;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (loginInfoMapper.selectExistUsername("admin")==0){
            LoginInfo loginInfo = new LoginInfo();
            loginInfo.setUsername("admin");
            loginInfo.setPassword(MD5.encode("admin123456"));
            loginInfo.setUserType(LoginInfo.USERTYPE_MGRSITE);
            loginInfo.setNickName("admin");
            loginInfoMapper.insert(loginInfo);
        }
    }
}
