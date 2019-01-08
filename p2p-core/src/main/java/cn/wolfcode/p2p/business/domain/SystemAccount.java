package cn.wolfcode.p2p.business.domain;

import cn.wolfcode.p2p.base.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 系统账户
 */
@Setter
@Getter
public class SystemAccount extends BaseDomain{

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 可用余额
     */
    private BigDecimal usableAmount;

    /**
     * 冻结金额
     */
    private BigDecimal freezedAmount;

}