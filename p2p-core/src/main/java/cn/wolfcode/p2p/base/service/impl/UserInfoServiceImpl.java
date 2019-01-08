package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.UserInfo;
import cn.wolfcode.p2p.base.exception.CustomException;
import cn.wolfcode.p2p.base.mapper.UserInfoMapper;
import cn.wolfcode.p2p.base.service.IUserInfoService;
import cn.wolfcode.p2p.util.BitStatesUtils;
import cn.wolfcode.p2p.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements IUserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public void save(UserInfo userInfo) {
        userInfoMapper.insert(userInfo);
    }

    @Override
    public UserInfo getById(Long id) {
        return userInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public void basicInfoSave(UserInfo userInfo) {
        UserInfo info = userInfoMapper.selectByPrimaryKey(UserContext.getLoginInfo().getId());
        info.setEducationBackground(userInfo.getEducationBackground());
        info.setHouseCondition(userInfo.getHouseCondition());
        info.setIncomeGrade(userInfo.getIncomeGrade());
        info.setKidCount(userInfo.getKidCount());
        info.setMarriage(userInfo.getMarriage());


        //设置位状态
        if (!info.hasBasicInfo()){
            info.setBitState(BitStatesUtils.addState(info.getBitState(),BitStatesUtils.OP_BASIC_INFO));
        }

        update(info);
    }

    @Override
    public void update(UserInfo info) {
        int row = userInfoMapper.updateByPrimaryKey(info);
        if (row==0){
            throw new CustomException("丢失更新");
        }
    }
}
