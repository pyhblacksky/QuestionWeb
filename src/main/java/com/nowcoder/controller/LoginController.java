package com.nowcoder.controller;

import com.nowcoder.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    //注册功能
    @RequestMapping(value = {"/reg/", "/reg"})
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password){
        try {
            Map<String, String> map = userService.register(username, password);
            //不为空，则存在问题
            if(map.containsKey("msg")){
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }
        } catch (Exception e){
            logger.error("注册异常 ： " + e);
            return "login";
        }

        //成功后返回主页
        return "redirect:/";
    }

    //注册界面
    @RequestMapping(value = {"/reglogin"}, method = {RequestMethod.GET})
    public String reglogin(){
        return "login";
    }

    /*
    //登录功能
    @RequestMapping(value = {""})
    public String login(){
        return
    }
    */
}
