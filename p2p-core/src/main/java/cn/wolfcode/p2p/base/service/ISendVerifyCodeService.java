package cn.wolfcode.p2p.base.service;

public interface ISendVerifyCodeService {
    /**
     * 发送验证码
     * @param phone 手机号
     */
    void send(String phone);
}
