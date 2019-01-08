package cn.wolfcode.p2p.business.domain;

import cn.wolfcode.p2p.base.domain.BaseDomain;
import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.util.Constants;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 针对一个还款计划,投资人的回款明细
 * 
 * @author Administrator
 * 
 */
@Setter
@Getter
public class PaymentScheduleDetail extends BaseDomain {

	/**
	 * 正常
	 */
	public static final int TRANSFER_STATE_NORMAL = 0;
	/**
	 * 转让中
	 */
	public static final int TRANSFER_STATE_TRANS = 1;

	// 该投标人总共投标金额,便于还款/垫付查询
	private BigDecimal bidAmount; 
	// 对应的投标ID
	private Long bidId;
	// 本期还款总金额(=本金+利息)
	private BigDecimal totalAmount = Constants.ZERO;
	// 本期应还款本金
	private BigDecimal principal = Constants.ZERO; 
	// 本期应还款利息
	private BigDecimal interest = Constants.ZERO; 
	// 第几期（即第几个月）
	private int monthIndex;
	// 本期还款截止时间
	private Date deadLine;
	// 所属哪个借款
	private Long bidRequestId;
	// 实际付款日期
	private Date payDate;
	// 还款方式
	private int returnType;
	// 所属还款计划
	private Long paymentScheduleId;
	// 还款人(即发标人)
	private LoginInfo fromLoginInfo;
	// 收款人(即投标人)
	private Long toLoginInfoId;
	//债权转让状态
	private Integer transferState = TRANSFER_STATE_NORMAL;

}
