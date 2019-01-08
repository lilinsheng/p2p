package cn.wolfcode.p2p.business.domain;

import cn.wolfcode.p2p.base.domain.BaseDomain;
import cn.wolfcode.p2p.base.domain.LoginInfo;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 线上充值记录
 */
@Setter
@Getter
public class RechargeOnline extends BaseDomain{
    public static final int STATE_NORMAL = 0;//处理中
    public static final int STATE_SUCCESS = 1;//成功
    public static final int STATE_FAILURE = 2;//失败

    //充值状态
    private Integer state = STATE_NORMAL;
    //失败信息
    private String remark;
    //申请时间
    private Date applyTime;
    //申请人
    private LoginInfo applier;
    //流水号
    private String tradeCode;
    //充值时间
    private Date tradeTime;
    //金额
    private BigDecimal amount;
}