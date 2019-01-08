package cn.wolfcode.p2p.business.domain;

import cn.wolfcode.p2p.base.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 系统转账流水
 */
@Setter
@Getter
public class SystemAccountFlow extends BaseDomain{
    //转账时间
    private Date actionTime;
    //类型
    private Integer actionType;
    //金额
    private BigDecimal amount;
    //备注
    private String note;
    //可用余额
    private BigDecimal usableAmount;
    //冻结金额
    private BigDecimal freezedAmount;

    /** ============系统账户流水类型============= */

    // 系统账户收到账户管理费（借款管理费）
    public final static int SYSTEM_ACCOUNT_ACTIONTYPE_MANAGE_CHARGE = 1;

    // 系统账户收到利息管理费
    public final static int SYSTEM_ACCOUNT_ACTIONTYPE_INTREST_MANAGE_CHARGE = 2;

    // 系统账户收到提现手续费
    public final static int SYSTEM_ACCOUNT_ACTIONTYPE_WITHDRAW_MANAGE_CHARGE = 3;

    // 扣除体验金利息费
    public final static int SYSTEM_ACCOUNT_ACTIONTYPE_EXP_INTEREST = 4;



}