package cn.wolfcode.p2p.business.query;

import cn.wolfcode.p2p.base.query.QueryObject;
import lombok.Getter;
import lombok.Setter;

/**
 * 债权转让查询条件
 */
@Setter
@Getter
public class TransferQueryObject extends QueryObject{
    //转让人id
    private Long toLoginInfoId;
    //债权标状态
    private Integer state = -1;
}
