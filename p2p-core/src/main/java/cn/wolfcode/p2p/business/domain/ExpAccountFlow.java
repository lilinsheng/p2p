package cn.wolfcode.p2p.business.domain;

import cn.wolfcode.p2p.base.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 体验金流水
 */
@Setter
@Getter
public class ExpAccountFlow extends BaseDomain{
    //流水类型
    private Integer actionType;
    //金额
    private BigDecimal amount;
    //备注
    private String note;
    //可用余额
    private BigDecimal usableAmount;
    //冻结金额
    private BigDecimal freezedAmount;
    //创建时间
    private Date actionTime;
    //体验金账户
    private ExpAccount expAccount;

}