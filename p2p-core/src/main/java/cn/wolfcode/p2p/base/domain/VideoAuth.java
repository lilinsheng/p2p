package cn.wolfcode.p2p.base.domain;

import com.alibaba.druid.support.json.JSONUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 视频认证对象
 */
@Setter
@Getter
public class VideoAuth extends BaseAuthDomain{

    //预约时间段
    private OrderTime orderTime;


    //预约日期
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date orderDate;


    public String getJsonString(){
        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        map.put("username",applier.getUsername());
        map.put("orderTime",getOrderTimeString());
        return JSONUtils.toJSONString(map);
    }

    public String getOrderTimeString(){
        StringBuilder sb = new StringBuilder();
        sb.append(DateFormatUtils.format(orderDate,"yyyy-MM-dd"))
                .append(" ")
                .append(orderTime.getBegin())
                .append(" - ")
                .append(orderTime.getEnd());
        return sb.toString();
    }

    public Date getBeginDate(){
        String[] strs = orderTime.getBegin().split(":");
        int h = Integer.parseInt(strs[0]);
        int m = Integer.parseInt(strs[1]);
        System.out.println(m);

        Date date = DateUtils.addHours(orderDate,h);
        date = DateUtils.addMinutes(date,m);
        return date;
    }
    public Date getEndDate(){
        String[] strs = orderTime.getEnd().split(":");
        int h = Integer.parseInt(strs[0]);
        int m = Integer.parseInt(strs[1]);
        System.out.println(m);

        Date date = DateUtils.addHours(orderDate,h);
        date = DateUtils.addMinutes(date,m);
        return date;
    }


}