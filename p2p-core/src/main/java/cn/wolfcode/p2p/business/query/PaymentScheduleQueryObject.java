package cn.wolfcode.p2p.business.query;

import cn.wolfcode.p2p.base.query.QueryObject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 还款计划查询
 */
@Setter
@Getter
public class PaymentScheduleQueryObject extends QueryObject{
    //全部
    private Integer state = -1;

    //标的类型
    private Integer bidRequestType = -1;

    //起始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;
    //结束时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    //借款用户id
    private Long borrowUserId;

}
