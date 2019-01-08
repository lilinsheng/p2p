package cn.wolfcode.p2p.business.mapper;

import cn.wolfcode.p2p.base.query.QueryObject;
import cn.wolfcode.p2p.business.domain.PlatformBankInfo;
import java.util.List;

public interface PlatformBankInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PlatformBankInfo record);

    PlatformBankInfo selectByPrimaryKey(Long id);

    List<PlatformBankInfo> selectAll();

    int updateByPrimaryKey(PlatformBankInfo record);

    int selectForCount(QueryObject qo);

    List selectForList(QueryObject qo);
}