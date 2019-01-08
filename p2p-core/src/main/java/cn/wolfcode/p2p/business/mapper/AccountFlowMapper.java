package cn.wolfcode.p2p.business.mapper;

import cn.wolfcode.p2p.business.domain.AccountFlow;
import java.util.List;

public interface AccountFlowMapper {

    int insert(AccountFlow record);

    AccountFlow selectByPrimaryKey(Long id);

    List<AccountFlow> selectAll();

    int updateByPrimaryKey(AccountFlow record);
}