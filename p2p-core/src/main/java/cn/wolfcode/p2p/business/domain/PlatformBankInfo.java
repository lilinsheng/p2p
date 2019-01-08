package cn.wolfcode.p2p.business.domain;

import cn.wolfcode.p2p.base.domain.BaseDomain;
import com.alibaba.druid.support.json.JSONUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * 平台银行账号
 */
@Setter
@Getter
public class PlatformBankInfo extends BaseDomain{
    //银行名称
    private String bankName;
    //账户名
    private String accountName;
    //卡号
    private String accountNumber;
    //分行
    private String bankForkName;

    public String getJsonString(){
        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        map.put("bankName",bankName);
        map.put("accountName",accountName);
        map.put("accountNumber",accountNumber);
        map.put("bankForkName",bankForkName);
        return JSONUtils.toJSONString(map);
    }

}