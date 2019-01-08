package cn.wolfcode.p2p.business.query;

import cn.wolfcode.p2p.base.query.QueryObject;
import lombok.Getter;
import lombok.Setter;

/**
 * 发标前审核
 */
@Setter
@Getter
public class BidRequestQueryObject extends QueryObject{


    //借款状态
    private Integer bidRequestState = -1;

    //标的类型
    private Integer bidRequestType = -1;

    //借款状态
    private int[] bidRequestStates;

    //排序规则
    private String orderByColumn;

    //排序类型
    private String orderType;

}
