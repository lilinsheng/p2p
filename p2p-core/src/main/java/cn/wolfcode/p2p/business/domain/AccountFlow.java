package cn.wolfcode.p2p.business.domain;

import cn.wolfcode.p2p.base.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 流水记录
 */
@Setter
@Getter
public class AccountFlow extends BaseDomain{

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
    //时间
    private Date actionTime;
    //账户id
    private Long accountId;

    /** =============================账户流水类型================================ */

    // 资金流水类别：线下充值
    // 可用余额增加
    public final static int ACCOUNT_ACTIONTYPE_RECHARGE_OFFLINE = 0;

    // 资金流水类别：提现成功
    // 冻结金额减少
    public final static int ACCOUNT_ACTIONTYPE_WITHDRAW = 1;

    // 资金流水类别：成功借款
    // 可用余额增加
    public final static int ACCOUNT_ACTIONTYPE_BIDREQUEST_SUCCESSFUL = 2;

    // 资金流水类别：成功投标
    // 冻结金额减少
    public final static int ACCOUNT_ACTIONTYPE_BID_SUCCESSFUL = 3;

    // 资金流水类别：还款
    // 可用余额减少
    public final static int ACCOUNT_ACTIONTYPE_RETURN_MONEY = 4;

    // 资金流水类别：回款
    // 可用余额增加
    public final static int ACCOUNT_ACTIONTYPE_CALLBACK_MONEY = 5;

    // 资金流水类别：支付平台管理费
    // 可用余额减少
    public final static int ACCOUNT_ACTIONTYPE_CHARGE = 6;

    // 资金流水类别：利息管理费
    // 可用余额减少
    public final static int ACCOUNT_ACTIONTYPE_INTEREST_SHARE = 7;

    // 资金流水类别：提现手续费
    // 冻结金额减少
    public final static int ACCOUNT_ACTIONTYPE_WITHDRAW_MANAGE_CHARGE = 8;

    // 资金流水类别：充值手续费
    // 可用余额减少
    public final static int ACCOUNT_ACTIONTYPE_RECHARGE_CHARGE = 9;

    // 资金流水类别：投标冻结金额
    // 冻结金额增加 可用余额减少
    public final static int ACCOUNT_ACTIONTYPE_BID_FREEZED = 10;

    // 资金流水类别：取消投标冻结金额
    // 标审核失败
    // 冻结金额减少
    // 可用余额增加
    public final static int ACCOUNT_ACTIONTYPE_BID_UNFREEZED = 11;

    // 资金流水类别：提现申请冻结金额
    // 冻结金额增加
    // 可用余额减少
    public final static int ACCOUNT_ACTIONTYPE_WITHDRAW_FREEZED = 12;

    // 资金流水类别:提现申请失败取消冻结金额
    // 冻结金额减少
    // 可用余额增加
    public final static int ACCOUNT_ACTIONTYPE_WITHDRAW_UNFREEZED = 13;

    //购买债券标：可用余额减少
    public final static int ACCOUNT_ACTIONTYPE_BUY_TRANSBID = 14;
    //出售债券标：可用余额增加
    public final static int ACCOUNT_ACTIONTYPE_SELL_TRANSBID = 15;
    //线上充值成功：可用余额增加
    public final static int ACCOUNT_ACTIONTYPE_RECHARGE_SUCCESS = 16;

}