package cn.wolfcode.p2p.business.domain;

import cn.wolfcode.p2p.base.domain.BaseDomain;
import cn.wolfcode.p2p.base.domain.LoginInfo;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
@Setter
@Getter
public class CreditTransfer extends BaseDomain {
private Integer version = 0;// 乐观锁
private Long bidId;// 对应投标id
private Long bidRequestId;// 对应借款
private BigDecimal bidRequestAmount;// 认购本金
private BigDecimal currentRate;// 借款利息
private Integer returnType;// 还款方式
private Integer monthIndex;// 总还款期数
private Integer remainMonthIndex;// 剩余还款期数
private BigDecimal remainInterest;// 剩余利息
private Date closestDeadLine;// 最近还款时间
private String bidRequestTitle;// 原借款名称
private Integer bidRequestState;// 债权状态
private LoginInfo transFrom;// 转出人
private LoginInfo transTo;// 接手人
private Date publishDate;// 发布时间
private Date transDate;// 接手时间
}
