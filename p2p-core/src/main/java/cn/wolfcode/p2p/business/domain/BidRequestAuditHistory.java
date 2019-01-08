package cn.wolfcode.p2p.business.domain;

import cn.wolfcode.p2p.base.domain.BaseAuthDomain;
import cn.wolfcode.p2p.base.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 借款申请审核历史
 */
@Setter
@Getter
public class BidRequestAuditHistory extends BaseAuthDomain{

    /**
     * 发标审核
     */
    public static final int AUDITTYPE_PUBLISH = 0;

    /**
     * 满标一审
     */
    public static final int AUDITTYPE_AUDIT1 = 1;

    /**
     * 满标二审
     */
    public static final int AUDITTYPE_AUDIT2 = 2;

    //借款申请id
    private Long bidRequestId;

    //审核状态
    private Integer auditType;
}