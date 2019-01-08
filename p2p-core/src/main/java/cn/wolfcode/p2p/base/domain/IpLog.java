package cn.wolfcode.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 登录日志
 */
@Getter
@Setter
public class IpLog extends BaseDomain{
    /**
     * 登录成功
     */
    public static final int STATE_SUCCESS = 0;

    /**
     * 登录失败
     */
    public static final int STATE_ERROR = 1;
    /**
     * 前台用户
     */
    public static final int USERTYPE_WEBSITE = 0;

    /**
     * 后台用户
     */
    public static final int USERTYPE_MGRSITE = 1;

    //ip
    private String ip;
    //登录状态
    private Byte state;
    //用户名
    private String username;
    //登录时间
    private Date loginTime;
    //用户类型
    private Integer userType = USERTYPE_WEBSITE;

    public String getStateName(){
        return state == STATE_SUCCESS?"登录成功":"登陆失败";
    }

    public String getUserTypeName(){
        return userType == USERTYPE_WEBSITE?"前台用户":"后台用户";
    }


}