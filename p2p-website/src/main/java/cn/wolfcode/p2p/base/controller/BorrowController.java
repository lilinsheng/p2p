package cn.wolfcode.p2p.base.controller;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.base.domain.UserInfo;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.service.IUserInfoService;
import cn.wolfcode.p2p.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 借款认证
 */
@Controller
public class BorrowController {

    @Autowired
    private IAccountService accountService;
    @Autowired
    private IUserInfoService userInfoService;

    /**
     * 进入贷款页面
     */
    @RequestMapping("/borrow")
    public String borrow(Model model){
        LoginInfo loginIngo = UserContext.getLoginInfo();
        //没有登录，跳转静态借款
        if (loginIngo==null){
            return "redirect:/borrow.html";
        }else {
            Account account = accountService.getById(loginIngo.getId());
            model.addAttribute("account",account);

            UserInfo userInfo = userInfoService.getById(loginIngo.getId());
            model.addAttribute("userInfo",userInfo);
            return "borrow";
        }
    }
}
