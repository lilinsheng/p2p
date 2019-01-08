package cn.wolfcode.p2p.business.domain;

import cn.wolfcode.p2p.base.domain.BaseDomain;
import cn.wolfcode.p2p.base.domain.LoginInfo;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 投资对象
 */
@Setter
@Getter
public class Bid extends BaseDomain{

    private BigDecimal actualRate;

    private BigDecimal availableAmount;

    private Long bidRequestId;

    private LoginInfo bidUser;

    private Date bidTime;

    private String bidRequestTitle;

    private Integer bidRequestState;
}