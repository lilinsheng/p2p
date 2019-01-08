package cn.wolfcode.p2p.base.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 验证码相关
 */
@Setter
@Getter
public class VerifyCodeVO {

    private String code;

    private Date sendTime;

    private String phone;
}
