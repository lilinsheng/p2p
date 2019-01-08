package cn.wolfcode.p2p.base.controller;

import cn.wolfcode.p2p.base.anno.NeedLogin;
import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.service.IIpLogService;
import cn.wolfcode.p2p.business.domain.ExpAccount;
import cn.wolfcode.p2p.business.service.IExpAccountService;
import cn.wolfcode.p2p.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 个人中心
 */
@Controller
public class PersonalController {
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IIpLogService ipLogService;
    @Autowired
    private IExpAccountService expAccountService;


    /**
     * 个人中心
     */
    @RequestMapping("/personal")
    @NeedLogin
    public String personal(Model model){
        LoginInfo loginInfo = UserContext.getLoginInfo();
        Account account = accountService.getById(loginInfo.getId());
        model.addAttribute("account",account);

        //体验金
        ExpAccount expAccount = expAccountService.getById(loginInfo.getId());
        model.addAttribute("expAccount",expAccount);

        //获取最后登录时间
        model.addAttribute("lastLoginTime",ipLogService.getLastLoginTime(UserContext.getLoginInfo().getUsername()));
        return "personal";
    }
}
