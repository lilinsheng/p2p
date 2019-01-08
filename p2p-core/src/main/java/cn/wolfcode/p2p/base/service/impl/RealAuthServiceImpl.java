package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.base.domain.RealAuth;
import cn.wolfcode.p2p.base.domain.UserInfo;
import cn.wolfcode.p2p.base.event.RealAuthSuccessEvent;
import cn.wolfcode.p2p.base.exception.CustomException;
import cn.wolfcode.p2p.base.mapper.RealAuthMapper;
import cn.wolfcode.p2p.base.mapper.UserInfoMapper;
import cn.wolfcode.p2p.base.query.RealAuthQueryObject;
import cn.wolfcode.p2p.base.service.IRealAuthService;
import cn.wolfcode.p2p.base.service.IUserInfoService;
import cn.wolfcode.p2p.util.Assert;
import cn.wolfcode.p2p.util.BitStatesUtils;
import cn.wolfcode.p2p.util.PageResult;
import cn.wolfcode.p2p.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class RealAuthServiceImpl implements IRealAuthService {

    @Autowired
    private RealAuthMapper realAuthMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private ApplicationContext ctx;

    @Override
    public void apply(RealAuth realAuth) {
        LoginInfo loginInfo = UserContext.getLoginInfo();
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(UserContext.getLoginInfo().getId());

        //参数校验,realName,sex,bornDate,idNumber,address,

        //判断是否完成了实名认证
        Assert.isFalse(userInfo.hasRealAuth()==true,"你已经完成了实名认证");

        //判断当前用户是否是未提交实名申请
        Assert.isFalse(userInfo.getRealAuthId()!=null,"你已经提交实名申请");

        //保存实名对象
        RealAuth newRealAuth = new RealAuth();
        newRealAuth.setRealName(realAuth.getRealName());
        newRealAuth.setSex(realAuth.getSex());
        newRealAuth.setIdNumber(realAuth.getIdNumber());
        newRealAuth.setBornDate(realAuth.getBornDate());
        newRealAuth.setAddress(realAuth.getAddress());
        newRealAuth.setState(RealAuth.STATE_NORMAL);
        newRealAuth.setImage1(realAuth.getImage1());
        newRealAuth.setImage2(realAuth.getImage2());
        newRealAuth.setApplier(loginInfo);
        newRealAuth.setApplyTime(new Date());
        realAuthMapper.insert(newRealAuth);

        //保存实名申请的id到userInfo的realAuthId上
        userInfo.setRealAuthId(newRealAuth.getId());
        userInfoService.update(userInfo);
    }

    @Override
    public PageResult queryForPage(RealAuthQueryObject qo) {
        int count = realAuthMapper.selectForCount(qo);
        if (count==0){
            return PageResult.empty(qo.getPageSize());
        }

        List listData = realAuthMapper.selectForList(qo);

        return new PageResult(listData,count,qo.getCurrentPage(),qo.getPageSize());
    }

    @Override
    public void realAuthAudit(Long realAuthId, Integer state, String remark) {
        LoginInfo loginInfo = UserContext.getLoginInfo();
        //参数校验realAuthId,state,remark

        //查看实名对象是否处于审核状态
        RealAuth realAuth = realAuthMapper.selectByPrimaryKey(realAuthId);
        Assert.isFalse(realAuth.getState()!=RealAuth.STATE_NORMAL,"实名对象不处于待审核状态");

        //更新实名对象信息
        realAuth.setAuditor(loginInfo);
        realAuth.setAuditTime(new Date());
        realAuth.setRemark(remark);
        realAuth.setState(state);
        update(realAuth);

        //申请人
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(realAuth.getApplier().getId());

        //更新userInfo对象
        if (state==RealAuth.STATE_SUCCESS){
            //设置真实姓名
            userInfo.setRealName(realAuth.getRealName());
            //设置身份证
            userInfo.setIdNumber(realAuth.getIdNumber());
            //添加身份认证的位状态
            userInfo.addBitState(BitStatesUtils.OP_REAL_AUTH);
            //发布认证成功事件
            ctx.publishEvent(new RealAuthSuccessEvent(this,realAuth));
        }else {
            //移除userInfo中的realAuthId
            userInfo.setRealAuthId(null);
        }
        userInfoService.update(userInfo);
    }

    @Override
    public void update(RealAuth realAuth) {
        int row = realAuthMapper.updateByPrimaryKey(realAuth);
        if (row==0){
            throw new CustomException("realAuth对象更新丢失");
        }
    }

    @Override
    public RealAuth getById(Long realAuthId) {
        return realAuthMapper.selectByPrimaryKey(realAuthId);
    }
}
