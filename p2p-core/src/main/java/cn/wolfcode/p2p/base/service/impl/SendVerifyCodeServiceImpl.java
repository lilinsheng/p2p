package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.exception.CustomException;
import cn.wolfcode.p2p.base.mapper.LoginInfoMapper;
import cn.wolfcode.p2p.base.service.ISendVerifyCodeService;
import cn.wolfcode.p2p.base.vo.VerifyCodeVO;
import cn.wolfcode.p2p.util.Assert;
import cn.wolfcode.p2p.util.DateUtil;
import cn.wolfcode.p2p.util.HttpUtil;
import cn.wolfcode.p2p.util.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static cn.wolfcode.p2p.enums.ErrorCode.*;
import static cn.wolfcode.p2p.util.Constants.*;

@Service
@Slf4j
public class SendVerifyCodeServiceImpl implements ISendVerifyCodeService {
    @Autowired
    private LoginInfoMapper loginInfoMapper;

    @Override
    public void send(String phone) {
        //1.检验手机号是否正确
        Assert.notNull(phone,LOGININFO_USERNAME_NULL.getErrorMsg());
        String phoneLengthMsg = MessageFormat.format(LOGININFO_USERNAME_LENGTH.getErrorMsg(), LENGTH_PHONE);
        Assert.length(phone,LENGTH_PHONE,phoneLengthMsg);
        Assert.phonePattern(phone,REGEX_MOBILE,LOGININFO_USERNAME_PATTERN.getErrorMsg());
        if (loginInfoMapper.selectExistUsername(phone)>0){
            throw new CustomException(LOGININFO_USERNAME_EXIST.getErrorMsg());
        }

        Date now = new Date();

        //2.发送验证码频繁判断
        VerifyCodeVO verifyCodeVO = UserContext.getVerifyCodeVO();
        if (verifyCodeVO!=null){
            Assert.isFalse(DateUtil.getSecondsBetween(verifyCodeVO.getSendTime(),now)<20,LOGININFO_VERIFYCODE_SEND.getErrorMsg());
        }

        //发送验证码
        String uuid = UUID.randomUUID().toString();
        String code = uuid.substring(0, 4);

        //发送验证码到手机
        String url = "http://utf8.api.smschinese.cn/";

        Map params = new HashMap();
        params.put("Uid","llls123");
        params.put("Key","d41d8cd98f00b204e980");
        params.put("smsMob",phone);
        params.put("smsText","p2p短信验证码为："+code+"，请在五分钟内使用");

        try {
            //String result = HttpUtil.sendHttpRequest(url, params);
            //int i= 1/0;
            String result="1";
            if (!result.equals("1")){
                throw new CustomException("获取验证码失败，请联系客服[119]");
            }
        } catch (CustomException e) {
            throw new CustomException(e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("获取验证码失败");
        }

        log.info("验证码为：{}",code);
        VerifyCodeVO vo = new VerifyCodeVO();
        vo.setCode(code);
        vo.setPhone(phone);
        vo.setSendTime(now);
        UserContext.setVerifyCodeVO(vo);
        System.out.println(UserContext.getVerifyCodeVO());
    }
}
