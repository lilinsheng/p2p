package cn.wolfcode.p2p.business.query;

import cn.wolfcode.p2p.base.query.QueryObject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * 线下审核查询相关
 */
@Setter
@Getter
public class RechargeOfflineQueryObject extends QueryObject{
    //审核状态
    private Integer state = -1;

    //起始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;
    //结束时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    //开户行id
    private Long bankInfoId;

    //交易号
    private String tradeCode;

    public String getTradeCode(){
        return StringUtils.hasLength(tradeCode)?tradeCode:null;
    }
}
