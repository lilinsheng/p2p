package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.UserInfo;

public interface IUserInfoService {
    /**
     * 新增用户基本信息
     */
    void save(UserInfo userInfo);

    /**
     * 查询用户信息
     */
    UserInfo getById(Long id);

    /**
     * 跟新基本信息
     */
    void basicInfoSave(UserInfo userInfo);

    /**
     * 跟新用户信息
     * @param userInfo
     */
    void update(UserInfo userInfo);
}
