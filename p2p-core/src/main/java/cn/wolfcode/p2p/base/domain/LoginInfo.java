package cn.wolfcode.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 登录信息
 */
@Setter
@Getter
public class LoginInfo {

    public final static int STATE_NORMAL = 0;//正常
    public final static int STATE_LOCK = 1;//锁定

    public final static int USERTYPE_WEBSITE = 0;//前台用户
    public final static int USERTYPE_MGRSITE = 1;//后台用户

    public final static int AUDITOR_NO = 0;//不是客服
    public final static int AUDITOR_YES = 1;//是客服



    private Long id;
    //昵称
    private String nickName;
    //用户名
    private String username;
    //密码
    private String password;
    //状态
    private Integer state = STATE_NORMAL;
    //用户类型
    private Integer userType = USERTYPE_WEBSITE;
    //是否是客服
    private Integer auditor = AUDITOR_NO;
}
