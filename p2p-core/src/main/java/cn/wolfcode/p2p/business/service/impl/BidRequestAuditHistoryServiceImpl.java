package cn.wolfcode.p2p.business.service.impl;

import cn.wolfcode.p2p.business.mapper.BidRequestAuditHistoryMapper;
import cn.wolfcode.p2p.business.service.IBidRequestAuditHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BidRequestAuditHistoryServiceImpl implements IBidRequestAuditHistoryService {
    @Autowired
    private BidRequestAuditHistoryMapper bidRequestAuditHistoryMapper;
}
