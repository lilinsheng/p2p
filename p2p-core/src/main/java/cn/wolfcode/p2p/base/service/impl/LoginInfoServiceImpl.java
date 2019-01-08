package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.base.domain.UserInfo;
import cn.wolfcode.p2p.base.exception.CustomException;
import cn.wolfcode.p2p.base.mapper.LoginInfoMapper;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.service.ILoginInfoService;
import cn.wolfcode.p2p.base.service.IUserInfoService;
import cn.wolfcode.p2p.base.vo.LoginInfoVO;
import cn.wolfcode.p2p.base.vo.VerifyCodeVO;
import cn.wolfcode.p2p.hosting.service.IHostingService;
import cn.wolfcode.p2p.util.Assert;
import cn.wolfcode.p2p.util.DateUtil;
import cn.wolfcode.p2p.util.MD5;
import cn.wolfcode.p2p.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import static cn.wolfcode.p2p.enums.ErrorCode.*;
import static cn.wolfcode.p2p.util.Constants.*;

@Service
public class LoginInfoServiceImpl implements ILoginInfoService {
    @Autowired
    private LoginInfoMapper loginInfoMapper;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IHostingService hostingService;

    @Override
    public LoginInfo getById(Long id) {
        return loginInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public void userRegister(LoginInfoVO loginInfoVO) {
        //注册校验
        registerCheck(loginInfoVO);


        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setNickName(loginInfoVO.getNickName());
        loginInfo.setUsername(loginInfoVO.getUsername());
        String enPwd = MD5.encode(loginInfoVO.getUsername() + loginInfoVO.getPassword());
        loginInfo.setPassword(enPwd);
        loginInfo.setState(LoginInfo.STATE_NORMAL);
        loginInfoMapper.insert(loginInfo);

        //新增账户
        Account account = new Account();
        account.setId(loginInfo.getId());
        accountService.save(account);

        //新增个人信息
        UserInfo userInfo = new UserInfo();
        userInfo.setPhoneNumber(loginInfo.getUsername());
        userInfo.setId(loginInfo.getId());
        userInfoService.save(userInfo);

        //注册托管账户
        hostingService.createAccount(loginInfo);
    }

    @Override
    public boolean existUsername(String username) {
        return loginInfoMapper.selectExistUsername(username)>0;
    }

    @Override
    public void login(LoginInfo loginInfo) {
        //登录参数校验
        loginCheck(loginInfo);

        //登录操作
        LoginInfo login = loginInfoMapper.selectByUsernameAndPassword(loginInfo.getUsername(),
                MD5.encode(loginInfo.getUsername()+loginInfo.getPassword()));

        //判断用户是否存在
        Assert.isFalse(login==null,"用户账号或密码错误");

        //判断用户是否锁定
        Assert.isFalse(login.getState()==LoginInfo.STATE_LOCK,"用户已锁定，请联系客服");

        //前台登录的状态为0，后台登录的类型为1
        Assert.isFalse(login.getUserType()!=loginInfo.getUserType(),"非法的用户");

        //设置当前登录信息到session
        UserContext.setLoginInfo(login);

    }

    @Override
    public List<LoginInfo> getAuditors() {
        return loginInfoMapper.selectAuditors();
    }

    private void loginCheck(LoginInfo loginInfo) {
        if (loginInfo.getUserType()==LoginInfo.USERTYPE_MGRSITE){
            Assert.notNull(loginInfo.getUsername(),"用户名不存在");
        }else {
            //校验手机号
            Assert.notNull(loginInfo.getUsername(),LOGININFO_USERNAME_NULL.getErrorMsg());
            String phoneLengthMsg = MessageFormat.format(LOGININFO_USERNAME_LENGTH.getErrorMsg(), LENGTH_PHONE);
            Assert.length(loginInfo.getUsername(),LENGTH_PHONE,phoneLengthMsg);
            Assert.phonePattern(loginInfo.getUsername(),REGEX_MOBILE,LOGININFO_USERNAME_PATTERN.getErrorMsg());
        }


        //校验密码
        Assert.notNull(loginInfo.getPassword(),LOGININFO_PASSWORD_NOLL.getErrorMsg());
        String passwordRangeMsg = MessageFormat.format(LOGININFO_PASSWORD_LENGTH.getErrorMsg(),LENGTH_MIN_PASSWORD,LENGTH_MAX_PASSWORD );
        Assert.range(loginInfo.getPassword(),LENGTH_MIN_PASSWORD,LENGTH_MAX_PASSWORD,passwordRangeMsg);
    }

    private void registerCheck(LoginInfoVO loginInfoVO) {
        //校验昵称
        Assert.notNull(loginInfoVO.getNickName(),LOGININFO_NICKNAME_NULL.getErrorMsg());
        String nickNameLengMsg = MessageFormat.format(LOGININFO_NICKNAME_LENGTH.getErrorMsg(), LENGTH_MIN_NICKNAME, LENGTH_MAX_NICKNAME);
        Assert.range(loginInfoVO.getNickName(),LENGTH_MIN_NICKNAME,LENGTH_MAX_NICKNAME,nickNameLengMsg);

        //校验手机号
        Assert.notNull(loginInfoVO.getUsername(),LOGININFO_USERNAME_NULL.getErrorMsg());
        String phoneLengthMsg = MessageFormat.format(LOGININFO_USERNAME_LENGTH.getErrorMsg(), LENGTH_PHONE);
        Assert.length(loginInfoVO.getUsername(),LENGTH_PHONE,phoneLengthMsg);
        Assert.phonePattern(loginInfoVO.getUsername(),REGEX_MOBILE,LOGININFO_USERNAME_PATTERN.getErrorMsg());
        if (loginInfoMapper.selectExistUsername(loginInfoVO.getUsername())>0){
            throw new CustomException(LOGININFO_USERNAME_EXIST.getErrorMsg());
        }

        //校验验证码
        Assert.notNull(loginInfoVO.getVerifycode(),LOGININFO_VERIFYCODE_NOLL.getErrorMsg());
        String verifycodeLengthMsg = MessageFormat.format(LOGININFO_VERIFYCODE_LENGTH.getErrorMsg(),LENGTH_VERIFYCODE);
        Assert.length(loginInfoVO.getVerifycode(),LENGTH_VERIFYCODE,verifycodeLengthMsg);

        //校验密码
        Assert.notNull(loginInfoVO.getPassword(),LOGININFO_PASSWORD_NOLL.getErrorMsg());
        String passwordRangeMsg = MessageFormat.format(LOGININFO_PASSWORD_LENGTH.getErrorMsg(),LENGTH_MIN_PASSWORD,LENGTH_MAX_PASSWORD );
        Assert.range(loginInfoVO.getPassword(),LENGTH_MIN_PASSWORD,LENGTH_MAX_PASSWORD,passwordRangeMsg);

        //校验确认密码
        Assert.notNull(loginInfoVO.getConfirmPwd(),LOGININFO_ConfirmPwd_NULL.getErrorMsg());
        Assert.isEquals(loginInfoVO.getPassword(),loginInfoVO.getConfirmPwd(),LOGININFO_ConfirmPwd_EQUALS.getErrorMsg());

        //验证码发送手机号一致校验
        VerifyCodeVO verifyCodeVO = UserContext.getVerifyCodeVO();
        Assert.isEquals(verifyCodeVO.getPhone(),loginInfoVO.getUsername(),"手机号不一致");

        //验证码准确性校验
        Assert.isEquals(verifyCodeVO.getCode(),loginInfoVO.getVerifycode(),"验证码错误");

        //验证码超时验证
        Assert.isFalse(DateUtil.getSecondsBetween(new Date(),verifyCodeVO.getSendTime())>300,"验证码已失效");
    }
}
