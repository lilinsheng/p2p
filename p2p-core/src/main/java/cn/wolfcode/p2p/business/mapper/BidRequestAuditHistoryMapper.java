package cn.wolfcode.p2p.business.mapper;

import cn.wolfcode.p2p.business.domain.BidRequestAuditHistory;
import java.util.List;

public interface BidRequestAuditHistoryMapper {

    int insert(BidRequestAuditHistory record);

    BidRequestAuditHistory selectByPrimaryKey(Long id);

    List<BidRequestAuditHistory> selectAll();
}