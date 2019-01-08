package cn.wolfcode.p2p.business.domain;

import cn.wolfcode.p2p.base.domain.BaseDomain;
import cn.wolfcode.p2p.base.domain.LoginInfo;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 体验金获取记录
 */
@Setter
@Getter
public class ExpAccountGrantRecord extends BaseDomain{
    //获取用户
    private LoginInfo grantUser;
    //金额
    private BigDecimal amount;
    //获取时间
    private Date grantDate;
    //返还时间
    private Date returnDate;
    //获取类型
    private Integer grantType;
    //备注
    private String note;
    //状态
    private Integer state = STATE_WAITING;

    public static final int GRANT_TYPE_GIFT = 0;//系统赠送体验金3000

    public static final int STATE_WAITING = 0;//待还
    public static final int STATE_RETURNED = 1;//已还
    public static final int STATE_ARREARS = 2;//欠费

}