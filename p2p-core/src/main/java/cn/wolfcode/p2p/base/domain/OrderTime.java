package cn.wolfcode.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 预约时间段
 */
@Setter
@Getter
public class OrderTime extends BaseDomain{

    //开始时间
    private String begin;

    //结束时间
    private String end;
}