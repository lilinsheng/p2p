package cn.wolfcode.p2p.hosting.service;

import cn.wolfcode.p2p.base.domain.LoginInfo;

import java.math.BigDecimal;

public interface IHostingService {
    /**
     * 创建托管账户
     */
    void createAccount(LoginInfo loginInfo);

    /**
     * 线上充值
     */
    String recharge(BigDecimal amount);

    /**
     * 跳转托管账户
     */
    String jumpAccount();

    /**
     * 设置支付密码
     */
    String setPayPassword();
}
