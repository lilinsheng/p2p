package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.base.domain.UserInfo;
import cn.wolfcode.p2p.base.domain.VideoAuth;
import cn.wolfcode.p2p.base.mapper.VideoAuthMapper;
import cn.wolfcode.p2p.base.query.VideoAuthQueryObject;
import cn.wolfcode.p2p.base.service.IUserInfoService;
import cn.wolfcode.p2p.base.service.IVideoAuthService;
import cn.wolfcode.p2p.util.Assert;
import cn.wolfcode.p2p.util.BitStatesUtils;
import cn.wolfcode.p2p.util.PageResult;
import cn.wolfcode.p2p.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class VideoAuthServiceImpl implements IVideoAuthService {
    @Autowired
    private VideoAuthMapper videoAuthMapper;

    @Autowired
    private IUserInfoService userInfoService;

    @Override
    public void apply(VideoAuth videoAuth) {
        //参数校验，auditor.id，orderDate，orderTime.id

        LoginInfo loginInfo = UserContext.getLoginInfo();
        UserInfo userInfo = userInfoService.getById(loginInfo.getId());

        //是否通过视频认证
        Assert.isFalse(userInfo.hasVedioAuth(),"你已通过视频认证");

        //是否已经有预约视频认证
        Assert.isFalse(userInfo.getVideoAuthId()!=null,"你已经有视频认证预约了");

        //保存视频认证信息
        VideoAuth va = new VideoAuth();
        va.setState(VideoAuth.STATE_NORMAL);
        va.setApplier(loginInfo);
        va.setAuditor(videoAuth.getAuditor());
        va.setApplyTime(new Date());
        va.setOrderDate(videoAuth.getOrderDate());
        va.setOrderTime(videoAuth.getOrderTime());
        videoAuthMapper.insert(va);

        //更新userInfo对象信息
        userInfo.setVideoAuthId(va.getId());
        userInfoService.update(userInfo);
    }

    @Override
    public VideoAuth getById(Long id) {
        return videoAuthMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageResult queryForPage(VideoAuthQueryObject qo) {
        int count = videoAuthMapper.selectForCount(qo);
        if (count==0){
            return PageResult.empty(qo.getPageSize());
        }

        List listData = videoAuthMapper.selectForList(qo);

        return new PageResult(listData,count,qo.getCurrentPage(),qo.getPageSize());
    }

    @Override
    public void videoAuth(Long id, Integer state, String remark) {
        //参数校验

        //判断申请人是否处于视频待审核阶段
        VideoAuth videoAuth = videoAuthMapper.selectByPrimaryKey(id);
        Assert.isFalse(videoAuth.getState()!=VideoAuth.STATE_NORMAL,"你不处于视频待审核阶段");

        //更新video对象
        videoAuth.setRemark(remark);
        videoAuth.setAuditTime(new Date());
        videoAuth.setState(state);
        update(videoAuth);

        //跟新userInfo对象
        UserInfo userInfo = userInfoService.getById(videoAuth.getApplier().getId());
        userInfo.addBitState(BitStatesUtils.OP_VEDIO_AUTH);
        userInfoService.update(userInfo);
    }

    public void update(VideoAuth videoAuth) {
        videoAuthMapper.updateByPrimaryKey(videoAuth);
    }
}
