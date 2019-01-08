package cn.wolfcode.p2p.base.query;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.util.Date;

@Setter
@Getter
public class VideoAuthQueryObject extends QueryObject{
    //审核状态
    private Integer state = -1;

    //起始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;
    //结束时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    //申请人名字
    private String username;
    //审核人id
    private Long auditorId;

    public String getUsername(){
        return StringUtils.hasLength(username)?username:null;
    }

}
