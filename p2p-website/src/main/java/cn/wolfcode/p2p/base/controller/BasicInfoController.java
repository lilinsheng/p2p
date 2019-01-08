package cn.wolfcode.p2p.base.controller;

import cn.wolfcode.p2p.base.anno.NeedLogin;
import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.domain.SystemDictionaryItem;
import cn.wolfcode.p2p.base.domain.UserInfo;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.service.ISystemDictionaryService;
import cn.wolfcode.p2p.base.service.IUserInfoService;
import cn.wolfcode.p2p.util.BitStatesUtils;
import cn.wolfcode.p2p.util.JsonResult;
import cn.wolfcode.p2p.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 基本信息
 */
@Controller
public class BasicInfoController {

    @Autowired
    private ISystemDictionaryService systemDictionaryService;

    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IAccountService accountService;

    /**
     * 完善基本信息
     */
    @RequestMapping("/basicInfo")
    @NeedLogin
    public String basicInfo(Model model){
        Account account = accountService.getById(UserContext.getLoginInfo().getId());
        model.addAttribute("account",account);

        UserInfo userInfo = userInfoService.getById(UserContext.getLoginInfo().getId());
        model.addAttribute("userInfo",userInfo);

        List<SystemDictionaryItem> educationBackgrounds =  systemDictionaryService.getBySn("educationBackgrounds");
        List<SystemDictionaryItem> incomeGrades =  systemDictionaryService.getBySn("incomeGrades");
        List<SystemDictionaryItem> marriages =  systemDictionaryService.getBySn("marriages");
        List<SystemDictionaryItem> kidCounts =  systemDictionaryService.getBySn("kidCounts");
        List<SystemDictionaryItem> houseConditions =  systemDictionaryService.getBySn("houseConditions");

        model.addAttribute("educationBackgrounds",educationBackgrounds);
        model.addAttribute("incomeGrades",incomeGrades);
        model.addAttribute("marriages",marriages);
        model.addAttribute("kidCounts",kidCounts);
        model.addAttribute("houseConditions",houseConditions);

        return "userInfo";
    }

    /**
     * 保存基本信息
     */
    @RequestMapping("/basicInfo_save")
    @ResponseBody
    public JsonResult basicInfoSave(UserInfo userInfo){

        userInfoService.basicInfoSave(userInfo);

        return JsonResult.instance();
    }
}
