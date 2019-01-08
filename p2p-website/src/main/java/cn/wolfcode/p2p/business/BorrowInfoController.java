package cn.wolfcode.p2p.business;

import cn.wolfcode.p2p.base.anno.NeedLogin;
import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.base.domain.RealAuth;
import cn.wolfcode.p2p.base.domain.UserInfo;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.service.IRealAuthService;
import cn.wolfcode.p2p.base.service.IUserInfoService;
import cn.wolfcode.p2p.business.domain.BidRequest;
import cn.wolfcode.p2p.business.domain.ExpAccount;
import cn.wolfcode.p2p.business.query.PaymentScheduleQueryObject;
import cn.wolfcode.p2p.business.service.IBidRequestService;
import cn.wolfcode.p2p.business.service.IBidService;
import cn.wolfcode.p2p.business.service.IExpAccountService;
import cn.wolfcode.p2p.business.service.IPaymentScheduleService;
import cn.wolfcode.p2p.util.JsonResult;
import cn.wolfcode.p2p.util.PageResult;
import cn.wolfcode.p2p.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

import static cn.wolfcode.p2p.util.Constants.*;

/**
 * 前台贷款
 */
@Controller
public class BorrowInfoController {

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IBidRequestService bidRequestService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IBidService bidService;

    @Autowired
    private IRealAuthService realAuthService;
    @Autowired
    private IPaymentScheduleService paymentScheduleService;
    @Autowired
    private IExpAccountService expAccountService;

    /**
     * 进入贷款页面
     */
    @RequestMapping("/borrowInfo")
    @NeedLogin
    public String borrowInfo(Model model){
        if (userInfoService.getById(UserContext.getLoginInfo().getId()).hasBidrequestInProcess()){
            return "borrow_apply_result";
        }

        //最小借款金额
        model.addAttribute("minBidRequestAmount",SMALLEST_BIDREQUEST_AMOUNT);

        //当前用户账户信息
        Account account = accountService.getById(UserContext.getLoginInfo().getId());
        model.addAttribute("account",account);

        //最小投标金额
        model.addAttribute("minBidAmount",SMALLEST_BID_AMOUNT);
        return "borrow_apply";
    }

    /**
     * 借款申请
     */
    @NeedLogin
    @RequestMapping("/borrow_apply")
    @ResponseBody
    public JsonResult borrowApply(BidRequest bidRequest){
        bidRequestService.apply(bidRequest);
        return JsonResult.instance();
    }

    /**
     * 进入投资详情页
     */
    @RequestMapping("/borrow_info")
    public String borrowInfo(Long id,Model model){
        //借款对象
        BidRequest bidRequest = bidRequestService.getById(id);
        model.addAttribute("bidRequest",bidRequest);

        if (bidRequest.getBidRequestType()==BidRequest.BID_TYPE_CREDIT){
            //信用标
            //借款人信息
            UserInfo userInfo = userInfoService.getById(bidRequest.getCreateUser().getId());
            model.addAttribute("userInfo",userInfo);

            //借款人实名认证信息
            RealAuth realAuth = realAuthService.getById(userInfo.getRealAuthId());
            model.addAttribute("realAuth",realAuth);
        }


        //登录状态访问
        LoginInfo loginInfo = UserContext.getLoginInfo();
        if (loginInfo!=null){
            if (loginInfo.getId()==bidRequest.getCreateUser().getId()){
                //查看自己的借款
                model.addAttribute("self",true);
            }else {
                //投资人查看

                if (bidRequest.getBidRequestType()==BidRequest.BID_TYPE_CREDIT){
                    //真实账户
                    Account account = accountService.getById(loginInfo.getId());
                    model.addAttribute("account",account);
                }else {
                    //体验金账户
                    ExpAccount account = expAccountService.getById(loginInfo.getId());
                    model.addAttribute("account",account);
                }
            }
        }


        return "borrow_info";
    }

    /**
     * 投资
     */
    @RequestMapping("/borrow_bid")
    @ResponseBody
    @NeedLogin
    public JsonResult borrowBid(Long bidRequestId, BigDecimal amount){
        bidService.bid(bidRequestId,amount);
        return JsonResult.instance();
    }

    /**
     * 还款计划列表
     */
    @RequestMapping("/borrowBidReturn_list")
    @NeedLogin
    public String borrowBidReturnist(@ModelAttribute("qo")PaymentScheduleQueryObject qo, Model model){
        Long loginInfoId = UserContext.getLoginInfo().getId();
        qo.setBorrowUserId(loginInfoId);
        PageResult pageResult = paymentScheduleService.queryForPage(qo);
        model.addAttribute("pageResult",pageResult);

        //账户
        Account account = accountService.getById(loginInfoId);
        model.addAttribute("account",account);
        return "returnmoney_list";
    }

    /**
     * 还款
     */
    @RequestMapping("/returnMoney")
    @ResponseBody
    @NeedLogin
    public JsonResult returnMoney(Long id){
        bidService.returnMoney(id);
        return JsonResult.instance();
    }
}
