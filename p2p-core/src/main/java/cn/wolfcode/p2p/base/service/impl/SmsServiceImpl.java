package cn.wolfcode.p2p.base.service.impl;


import cn.wolfcode.p2p.base.domain.RealAuth;
import cn.wolfcode.p2p.base.event.RealAuthSuccessEvent;
import cn.wolfcode.p2p.base.service.ISmsService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SmsServiceImpl implements ISmsService,ApplicationListener<RealAuthSuccessEvent> {
    @Override
    public void onApplicationEvent(RealAuthSuccessEvent realAuthSuccessEvent) {
        sendSms(realAuthSuccessEvent.getRealAuth());
    }

    private void sendSms(RealAuth realAuth) {
        System.out.println(realAuth.getRealName()+"：实名认证成功");
    }
}
