package cn.wolfcode.p2p.business;

import cn.wolfcode.p2p.base.anno.NeedLogin;
import cn.wolfcode.p2p.business.query.TransferQueryObject;
import cn.wolfcode.p2p.business.service.ICreditTransferService;
import cn.wolfcode.p2p.util.Constants;
import cn.wolfcode.p2p.util.JsonResult;
import cn.wolfcode.p2p.util.PageResult;
import cn.wolfcode.p2p.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 债权转让
 */
@Controller
public class TransferController {

    @Autowired
    private ICreditTransferService creditTransferService;

    /**
     * 可转让债权
     */
    @RequestMapping("/listCanTransferCredit")
    @NeedLogin
    public String listCanTransferCredit(@ModelAttribute("qo")TransferQueryObject qo, Model model){
        //查询可转让的债权
        qo.setToLoginInfoId(UserContext.getLoginInfo().getId());
        PageResult pageResult = creditTransferService.queryForCanTransPage(qo);
        model.addAttribute("pageResult",pageResult);
        return "credittransfer_cantrans_list";
    }

    /**
     * 转让债权
     */
    @RequestMapping("/creditTransfer")
    @ResponseBody
    @NeedLogin
    public JsonResult creditTransfer(Long[] bidId){
        creditTransferService.creditTransfer(bidId);
        return JsonResult.instance();
    }

    /**
     * 购买债权标
     */
    @RequestMapping("/subscribe")
    @ResponseBody
    @NeedLogin
    public JsonResult subscribe(Long id){
        creditTransferService.subscribe(id);
        return JsonResult.instance();
    }

    /**
     * 自己转让中的债权列表
     */
    @RequestMapping("/listMyTransferCredit")
    @NeedLogin
    public String listMyTransferCredit(@ModelAttribute("qo")TransferQueryObject qo,Model model){
        qo.setState(Constants.TRANSFER_STATE_TRANSING);
        qo.setToLoginInfoId(UserContext.getLoginInfo().getId());
        PageResult pageResult = creditTransferService.queryForMyTransPage(qo);
        model.addAttribute("pageResult",pageResult);
        return "credittransfer_mytrans_list";
    }

    /**
     * 撤消债权标
     */
    @RequestMapping("/cancelMyTrans")
    @NeedLogin
    @ResponseBody
    public JsonResult cancelMyTrans(Long bidId){
        creditTransferService.cancelTrans(bidId);
        return JsonResult.instance();
    }
}
