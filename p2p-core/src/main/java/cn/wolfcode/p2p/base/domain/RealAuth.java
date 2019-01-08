package cn.wolfcode.p2p.base.domain;

import com.alibaba.druid.support.json.JSONUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.SimpleFormatter;

/**
 * 实名认证
 */
@Setter
@Getter
public class
RealAuth extends BaseAuthDomain{

    /**
     * 性别 ：男
     */
    public static final int SEX_MALE = 0 ;
    /**
     * 性别 ：女
     */
    public static final int SEX_FEMALE = 1 ;



    //真实姓名
    private String realName;

    //版本号
    private Integer version;

    //性别
    private Integer sex;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date bornDate;

    //身份证
    private String idNumber;

    //地址
    private String address;



    //正面
    private String image1;

    //反面
    private String image2;





    public String getSexDisplay(){
        return sex==SEX_MALE?"男":"女";
    }



    public String getJsonString(){
        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        map.put("username",applier.getUsername());
        map.put("realname",realName);
        map.put("idNumber",idNumber);
        map.put("address",address);
        map.put("sex",getSexDisplay());
        map.put("birthDate", DateFormatUtils.format(bornDate,"yyyy-MM-dd"));
        map.put("image1",image1);
        map.put("image2",image2);
        return JSONUtils.toJSONString(map);
    }
}