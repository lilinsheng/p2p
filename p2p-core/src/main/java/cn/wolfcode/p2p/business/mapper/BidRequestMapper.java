package cn.wolfcode.p2p.business.mapper;

import cn.wolfcode.p2p.business.domain.BidRequest;
import cn.wolfcode.p2p.business.query.BidRequestQueryObject;
import java.util.List;

public interface BidRequestMapper {

    int insert(BidRequest record);

    BidRequest selectByPrimaryKey(Long id);

    int updateByPrimaryKey(BidRequest record);

    int selectForCount(BidRequestQueryObject qo);

    List selectForList(BidRequestQueryObject qo);
}