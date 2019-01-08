package cn.wolfcode.p2p.business;

import cn.wolfcode.p2p.base.anno.NeedLogin;
import cn.wolfcode.p2p.business.service.IExpAccountService;
import cn.wolfcode.p2p.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 体验标
 */
@Controller
public class ExpBidRequestController {

    @Autowired
    private IExpAccountService expAccountService;

    /**
     * 手动领取体验金
     */
    @RequestMapping("/expGold")
    @ResponseBody
    @NeedLogin
    public JsonResult expGold(){
        expAccountService.getExpGold();
        return JsonResult.instance();
    }
}
