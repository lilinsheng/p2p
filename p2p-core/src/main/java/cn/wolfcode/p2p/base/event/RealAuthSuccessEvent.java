package cn.wolfcode.p2p.base.event;

import cn.wolfcode.p2p.base.domain.RealAuth;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 实名认证成功事件
 */
@Getter
public class RealAuthSuccessEvent extends ApplicationEvent{
    private RealAuth realAuth;

    public RealAuthSuccessEvent(Object source,RealAuth realAuth) {
        super(source);
        this.realAuth = realAuth;
    }
}
