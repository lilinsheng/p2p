package cn.wolfcode.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 认证基本信息
 */
@Setter
@Getter
public class BaseAuthDomain extends BaseDomain{

    /**
     * 状态：待审核
     */
    public static final int STATE_NORMAL = 0 ;
    /**
     * 审核通过
     */
    public static final int STATE_SUCCESS = 1 ;
    /**
     * 审核拒绝
     */
    public static final int STATE_REJECT = 2 ;

    //状态
    protected Integer state = STATE_NORMAL;

    //备注
    protected String remark;

    //审核时间
    protected Date auditTime;

    //申请时间
    protected Date applyTime;

    //审核人
    protected LoginInfo auditor;

    //申请人
    protected LoginInfo applier;

    public String getStateDisplay(){
        switch (state){
            case STATE_NORMAL:return "待审核";
            case STATE_SUCCESS:return "审核通过";
            case STATE_REJECT:return "审核拒绝";
        }
        return "";
    }
}
