package cn.wolfcode.p2p.base.query;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 实名审核查询相关
 */
@Setter
@Getter
public class RealAuthQueryObject extends QueryObject{
    //审核状态
    private Integer state = -1;

    //起始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;
    //结束时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
}
