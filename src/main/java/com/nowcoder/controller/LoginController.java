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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    //注册功能
    @RequestMapping(value = {"/reg/"})
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "remeberme", defaultValue = "false") boolean rememberme,
                      HttpServletResponse response){
        try {
            Map<String, String> map = userService.register(username, password);
            //包含ticket，说明已注册，需要跳转并保存t
            if(map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);
                //成功后返回主页
                return "redirect:/";
            } else{
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }
        } catch (Exception e){
            logger.error("注册异常 ： " + e);
            return "login";
        }

    }

    //注册界面
    @RequestMapping(value = {"/reglogin"}, method = {RequestMethod.GET})
    public String reglogin(){
        return "login";
    }


    //登录功能
    @RequestMapping(value = {"/login", "/login/"})
    public String login(Model model, @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "remeberme", defaultValue = "false") boolean rememberme,
                        HttpServletResponse response){
        try {
            Map<String, String> map = userService.login(username, password);
            if(map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);
                return "redirect:/";
            } else{
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
        }catch (Exception e){
            logger.error("登录失败 ： " + e.toString());
            return "login";
        }
    }

}
