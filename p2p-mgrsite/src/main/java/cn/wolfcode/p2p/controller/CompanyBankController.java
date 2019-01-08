package cn.wolfcode.p2p.controller;

import cn.wolfcode.p2p.base.query.QueryObject;
import cn.wolfcode.p2p.business.domain.PlatformBankInfo;
import cn.wolfcode.p2p.business.service.IPlatformBankInfoService;
import cn.wolfcode.p2p.util.JsonResult;
import cn.wolfcode.p2p.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 平台银行账号管理
 */
@Controller
public class CompanyBankController {

    @Autowired
    private IPlatformBankInfoService platformBankInfoService;

    /**
     * 平台银行账号列表
     */
    @RequestMapping("/companyBank_list")
    public String companyBankList(@ModelAttribute("qo") QueryObject qo, Model model){
        PageResult pageResult = platformBankInfoService.queryForPage(qo);
        model.addAttribute("pageResult",pageResult);
        return "platformbankinfo/list";
    }

    /**
     * 保存或更新平台银行信息
     */
    @RequestMapping("/companyBank_update")
    @ResponseBody
    public JsonResult companyBankUpdate(PlatformBankInfo platformBankInfo){
        platformBankInfoService.saveOrUpdate(platformBankInfo);
        return JsonResult.instance();
    }
}
