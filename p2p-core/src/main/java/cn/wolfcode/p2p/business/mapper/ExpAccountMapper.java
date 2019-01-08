package cn.wolfcode.p2p.business.mapper;

import cn.wolfcode.p2p.business.domain.ExpAccount;
import java.util.List;

public interface ExpAccountMapper {
    int insert(ExpAccount record);

    ExpAccount selectByPrimaryKey(Long id);

    int updateByPrimaryKey(ExpAccount record);
}