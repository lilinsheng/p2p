package cn.wolfcode.p2p.business.domain;

import cn.wolfcode.p2p.base.domain.BaseDomain;
import cn.wolfcode.p2p.base.domain.LoginInfo;
import com.alibaba.druid.support.json.JSONUtils;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.*;

import static cn.wolfcode.p2p.util.Constants.*;

/**
 * 借款对象
 */
@Setter
@Getter
public class BidRequest extends BaseDomain{

    public static final int BID_TYPE_CREDIT = 0;//信用标
    public static final int BID_TYPE_CLAIM = 1;//债权标
    public static final int BID_TYPE_EXP = 2;//体验标



    //版本号
    private Integer version = 0;

    //借款类型，在我们项目中，就是信用借款
    private Integer bidRequestType = BID_TYPE_CREDIT;

    //借款的的状态
    private Integer bidRequestState;

    //借款的金额
    private BigDecimal bidRequestAmount = SMALLEST_BIDREQUEST_AMOUNT;

    //借款的利率
    private BigDecimal currentRate;

    //借款期限
    private Integer returnMonthes;

    //借款现在已经有多少次投标
    private Integer bidCount = 0;

    //总回报金额
    private BigDecimal totalRewardAmount;

    //已经投了多少钱
    private BigDecimal currentSum = new BigDecimal("0");

    //借款标题
    private String title;

    //描述
    private String description;

    //风控评审意见
    private String note;

    //招标到期时间
    private Date disableDate;

    //借款人
    private LoginInfo createUser;

    //招标天数
    private Integer disableDays;

    //借款允许的最小的投标金额，默认是50；
    private BigDecimal minBidAmount;

    //申请时间
    private Date applyTime;

    //发布时间
    private Date publishTime;

    //还款方式
    private Integer returnType;

    //投资记录
    private List<Bid> bids = new ArrayList<>();

    public int getPersent(){
        return currentSum.divide(bidRequestAmount,SCALE_CAL,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).intValue();
    }

    //还需金额
    public BigDecimal getRemainAmount(){
        return bidRequestAmount.subtract(currentSum);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BidRequest that = (BidRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(version);
    }

    public String getReturnTypeDisplay(){
        return returnType==RETURN_TYPE_MONTH_INTEREST_PRINCIPAL?"按月分期":"按月到期";
    }

    public String getBidRequestStateDisplay(){
        switch (bidRequestState){
            case BIDREQUEST_STATE_APPLY:return "发标待审";
            case BIDREQUEST_STATE_PUBLISH_PENDING:return "待发布";
            case BIDREQUEST_STATE_BIDDING:return "招标中";
            case BIDREQUEST_STATE_UNDO:return "已撤销";
            case BIDREQUEST_STATE_BIDDING_OVERDUE   :return "流标";
            case BIDREQUEST_STATE_APPROVE_PENDING_1 :return "满标1审";
            case BIDREQUEST_STATE_APPROVE_PENDING_2  :return "满标2审";
            case BIDREQUEST_STATE_REJECTED         :return "满标审核被拒绝";
            case BIDREQUEST_STATE_PAYING_BACK     :return "还款中";
            case BIDREQUEST_STATE_COMPLETE_PAY_BACK :return "已还清";
            case BIDREQUEST_STATE_PAY_BACK_OVERDUE  :return "逾期";
            case BIDREQUEST_STATE_PUBLISH_REFUSE   :return "发标审核拒绝状态";
        }
        return "状态异常";
    }

    public String getJsonString(){
        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        map.put("username",createUser.getUsername());
        map.put("title",title);
        map.put("bidRequestAmount",bidRequestAmount);
        map.put("currentRate",currentRate);
        map.put("returnMonthes",returnMonthes);
        map.put("returnType",returnType);
        map.put("totalRewardAmount",totalRewardAmount);
        return JSONUtils.toJSONString(map);
    }
}