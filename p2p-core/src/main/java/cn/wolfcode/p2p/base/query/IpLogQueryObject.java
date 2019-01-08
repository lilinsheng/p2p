package cn.wolfcode.p2p.base.query;

import cn.wolfcode.p2p.util.DateUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.util.Date;

@Setter
@Getter
public class IpLogQueryObject extends QueryObject{
    //起始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;
    //结束时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    //登录状态
    private Integer state = -1;
    //当前登录用户
    private String username;
    //用户类型
    private Integer userType = -1;

    public Date getBeginDate(){
        return DateUtil.getBeginDate(beginDate);
    }

    public Date getEndDate(){
        return DateUtil.getEndDate(endDate);
    }

    public String getUsername(){
        return StringUtils.hasLength(username)?username:null;
    }
}
