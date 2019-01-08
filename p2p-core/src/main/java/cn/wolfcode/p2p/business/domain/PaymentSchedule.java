package cn.wolfcode.p2p.business.domain;

import cn.wolfcode.p2p.base.domain.BaseDomain;
import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.util.Constants;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * 还款计划对象，记录借款每月的还款信息(该信息针对借款用户，paymentscheduledetail针对投资人)
 * 
 * @author stef
 */
@Setter
@Getter
public class PaymentSchedule extends BaseDomain {

	// 对应借款
	private Long bidRequestId;
	//借款名称
	private String bidRequestTitle;
	// 还款人
	private LoginInfo borrowUser;
	// 本期还款截止期限
	private Date deadLine;
	// 还款时间
	private Date payDate;

	// 本期还款总金额，利息 +本金
	private BigDecimal totalAmount = Constants.ZERO;
	// 本期还款本金
	private BigDecimal principal = Constants.ZERO; 
	// 本期还款总利息
	private BigDecimal interest = Constants.ZERO; 
	// 第几期 (即第几个月)
	private int monthIndex;
	// 本期还款状态（默认正常待还）
	private int state = Constants.PAYMENT_STATE_NORMAL; 
	// 借款类型
	private int bidRequestType;
	// 还款方式，等同借款(BidRequest)中的还款方式
	private int returnType;
	//滞纳金
	private BigDecimal overdueAmount;

	// 本期还款计划对应的收款计划明细
	private List<PaymentScheduleDetail> paymentScheduleDetails = new ArrayList<PaymentScheduleDetail>();

	public String getStateDisplay() {
		switch (state) {
		case Constants.PAYMENT_STATE_NORMAL:
			return "正常待还";
		case Constants.PAYMENT_STATE_DONE:
			return "已还";
		case Constants.PAYMENT_STATE_OVERDUE:
			return "逾期";
		default:
			return "未知";
		}
	}
}
