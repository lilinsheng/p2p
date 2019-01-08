package cn.wolfcode.p2p.business.domain;

import cn.wolfcode.p2p.base.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 体验金账户
 */
@Setter
@Getter
public class ExpAccount extends BaseDomain{

    //版本号
    private Integer version = 0;
    //可用余额：体验金
    private BigDecimal usableAmount;
    //冻结金额：体验金
    private BigDecimal freezedAmount = BigDecimal.ZERO;
    //未还体验金
    private BigDecimal unReturnExpAmount = BigDecimal.ZERO;
}