package cn.wolfcode.p2p.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 后台主页
 */
@Controller
public class MainController {

    /**
     * 主页
     */
    @RequestMapping("/main")
    public String main(){
        return "main";
    }
}
