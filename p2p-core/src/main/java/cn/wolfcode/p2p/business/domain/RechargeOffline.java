package cn.wolfcode.p2p.business.domain;

import cn.wolfcode.p2p.base.domain.BaseAuthDomain;
import com.alibaba.druid.support.json.JSONUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 线下充值申请
 */
@Setter
@Getter
public class RechargeOffline extends BaseAuthDomain{
    //流水号
    private String tradeCode;
    //充值时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date tradeTime;
    //充值金额
    private BigDecimal amount;
    //充值备注
    private String note;
    //对应银行卡
    private PlatformBankInfo bankInfo;


    public String getJsonString(){
        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        map.put("username",applier.getUsername());
        map.put("tradeCode",tradeCode);
        map.put("amount",amount);
        map.put("tradeTime",tradeTime);
        return JSONUtils.toJSONString(map);
    }
}