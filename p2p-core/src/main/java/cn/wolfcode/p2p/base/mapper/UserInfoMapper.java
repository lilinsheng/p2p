package cn.wolfcode.p2p.base.mapper;

import cn.wolfcode.p2p.base.domain.UserInfo;
import java.util.List;

public interface UserInfoMapper {

    int insert(UserInfo record);

    UserInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKey(UserInfo record);

}