package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.base.vo.LoginInfoVO;

import java.util.List;

public interface ILoginInfoService {
    /**
     * 根据id查询登录信息
     */
    LoginInfo getById(Long id);

    /**
     * 用户注册
     */
    void userRegister(LoginInfoVO loginInfoVO);

    /**
     * 查看是否存在该用户
     */
    boolean existUsername(String username);

    /**
     * 用户登录
     */
    void login(LoginInfo loginInfo);

    /**
     * 获取预约客服
     */
    List<LoginInfo> getAuditors();
}
