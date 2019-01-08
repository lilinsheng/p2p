package cn.wolfcode.p2p.base.controller;

import cn.wolfcode.p2p.base.anno.NeedLogin;
import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.base.domain.RealAuth;
import cn.wolfcode.p2p.base.domain.UserInfo;
import cn.wolfcode.p2p.base.service.IRealAuthService;
import cn.wolfcode.p2p.base.service.IUserInfoService;
import cn.wolfcode.p2p.util.JsonResult;
import cn.wolfcode.p2p.util.UploadUtil;
import cn.wolfcode.p2p.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 实名认证
 */
@Controller
public class RealAuthController {

    @Autowired
    private IRealAuthService realAuthService;
    @Autowired
    private IUserInfoService userInfoService;

    /**
     * 进入实名认证页面
     */
    @NeedLogin
    @RequestMapping("/realAuth")
    public String realAuth(Model model){
        LoginInfo loginInfo = UserContext.getLoginInfo();
        //获取当前对象
        UserInfo userInfo = userInfoService.getById(loginInfo.getId());

        //当用户已经实名
        if (userInfo.hasRealAuth()){
            RealAuth realAuth = realAuthService.getById(userInfo.getRealAuthId());
            model.addAttribute("realAuth",realAuth);
            return "realAuth_result";
        }

        //当用户已申请实名
        if (userInfo.getRealAuthId()!=null){
            model.addAttribute("auditing",true);
            return "realAuth_result";
        }
        return "realAuth";
    }

    //保存实名申请
    @NeedLogin
    @RequestMapping("/realAuth_save")
    @ResponseBody
    public JsonResult realAuthSave(RealAuth realAuth){
        realAuthService.apply(realAuth);
        return JsonResult.instance() ;
    }

    @Value("${p2p.upload}")
    private String p2pUpload;

    /**
     * 身份证照上传
     */
    @NeedLogin
    @RequestMapping("/uploadPhoto")
    @ResponseBody
    public String uploadPhoto(MultipartFile file){
        String upload = UploadUtil.upload(file, p2pUpload);
        return upload;
    }
}
