package cn.wolfcode.p2p.hosting.service.impl;
import java.util.Date;

import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.base.exception.CustomException;
import cn.wolfcode.p2p.business.domain.RechargeOnline;
import cn.wolfcode.p2p.business.service.IRechargeOnlineService;
import cn.wolfcode.p2p.hosting.service.IHostingService;
import cn.wolfcode.p2p.hosting.util.CallServiceUtil;
import cn.wolfcode.p2p.hosting.util.HostingUtil;
import cn.wolfcode.p2p.util.Assert;
import cn.wolfcode.p2p.util.UserContext;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class HostingServiceImpl implements IHostingService {

    @Autowired
    private IRechargeOnlineService rechargeOnlineService;

    @Override
    public void createAccount(LoginInfo loginInfo) {
        //设置基本参数
        Map<String,String> params =HostingUtil.getBaseParams();
        params.put("service","create_activate_member");


        //业务参数
        params.put("identity_id","wolf_"+loginInfo.getId());
        params.put("identity_type","UID");
        params.put("member_type","1");
        params.put("client_ip", UserContext.getIP());

        //签名
        String sign = HostingUtil.getSign(params);
        params.put("sign",sign);

        //拼接请求参数
        String paramLink = HostingUtil.getRequestUrl(params);

        //发送请求
        String result = CallServiceUtil.sendPost(HostingUtil.URL_MGS, paramLink);

        try {
            result = URLDecoder.decode(result, HostingUtil.CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Map<String,String> map = JSON.parseObject(result, Map.class);
        //校验回签
        boolean success = HostingUtil.checkSign(map);

        if (success){
            log.info("托管注册会员成功");
        }else {
            Assert.isFalse(!success,"回签校验失败");
        }
    }

    @Override
    public String recharge(BigDecimal amount) {
        LoginInfo loginInfo = UserContext.getLoginInfo();

        //判断金额不能为0或null
        Assert.notNull(amount,"金额不能为空");
        Assert.isFalse(amount.compareTo(new BigDecimal("0"))==0,"金额不能为0");

        //保存线上充值记录
        RechargeOnline ro = new RechargeOnline();
        ro.setApplyTime(new Date());
        ro.setApplier(loginInfo);
        ro.setTradeCode(UUID.randomUUID().toString());
        ro.setAmount(amount);
        rechargeOnlineService.save(ro);

        //设置基本参数
        Map<String,String> params =HostingUtil.getBaseParams();
        params.put("service","create_hosting_deposit");
        params.put("notify_url","http://localhost/notifySuccess");
        params.put("return_url","http://localhost/returnSuccess?tradeCode="+ro.getTradeCode());

        //设置业务参数
        params.put("out_trade_no",ro.getTradeCode());
        params.put("summary","账户充值");
        params.put("identity_id","wolf_"+loginInfo.getId());
        params.put("identity_type","UID");
        params.put("account_type","BASIC");
        params.put("amount",amount.toString());
        params.put("client_ip", UserContext.getIP());
        params.put("pay_method", "SINAPAY^"+amount.toString());

        //签名
        String sign = HostingUtil.getSign(params);
        params.put("sign",sign);

        //拼接请求参数
        String paramLink = HostingUtil.getRequestUrl(params);

        //发送请求
        String result = CallServiceUtil.sendPost(HostingUtil.URL_MAS, paramLink);

        try {
            result = URLDecoder.decode(result, HostingUtil.CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (result.indexOf("response_code")!=-1){
            Map<String,String> map = JSON.parseObject(result, Map.class);
            throw new CustomException(map.get("response_message"));
        }

        return result;

    }

    @Override
    public String jumpAccount() {
        LoginInfo loginInfo = UserContext.getLoginInfo();
        //设置基本参数
        Map<String,String> params =HostingUtil.getBaseParams();
        params.put("service","show_member_infos_sina");


        //业务参数
        params.put("identity_id","wolf_"+loginInfo.getId());
        params.put("identity_type","UID");
        params.put("resp_method","1");
        params.put("default_page","DEFAULT");

        //签名
        String sign = HostingUtil.getSign(params);
        params.put("sign",sign);

        //拼接请求参数
        String paramLink = HostingUtil.getRequestUrl(params);

        //发送请求
        String result = CallServiceUtil.sendPost(HostingUtil.URL_MGS, paramLink);

        try {
            result = URLDecoder.decode(result, HostingUtil.CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Map<String,String> map = JSON.parseObject(result, Map.class);
        //校验回签
        boolean success = HostingUtil.checkSign(map);

        if (success){
            log.info("请求进入托管会员中心成功");
        }else {
            Assert.isFalse(!success,"回签校验失败");
        }
        return map.get("redirect_url");
    }

    @Override
    public String setPayPassword() {
        LoginInfo loginInfo = UserContext.getLoginInfo();
        //设置基本参数
        Map<String,String> params =HostingUtil.getBaseParams();
        params.put("service","set_pay_password");
        params.put("return_url","http://localhost/return_payPassword?mark="+loginInfo.getId());


        //业务参数
        params.put("identity_id","wolf_"+loginInfo.getId());
        params.put("identity_type","UID");
        params.put("client_ip", UserContext.getIP());

        //签名
        String sign = HostingUtil.getSign(params);
        params.put("sign",sign);

        //拼接请求参数
        String paramLink = HostingUtil.getRequestUrl(params);

        //发送请求
        String result = CallServiceUtil.sendPost(HostingUtil.URL_MGS, paramLink);

        try {
            result = URLDecoder.decode(result, HostingUtil.CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Map<String,String> map = JSON.parseObject(result, Map.class);
        //校验回签
        boolean success = HostingUtil.checkSign(map);

        if (success){
            log.info("申请支付密码请求成功");
        }else {
            Assert.isFalse(!success,"回签校验失败");
        }

        return map.get("redirect_url");
    }
}
