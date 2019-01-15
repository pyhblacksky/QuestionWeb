package com.nowcoder.controller;

import com.nowcoder.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    //注册功能
    @RequestMapping(value = {"/reg/","/reg"})
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "next",required = false) String next,
                      @RequestParam(value = "remeberme", defaultValue = "false") boolean rememberme,
                      HttpServletResponse response){
        try {
            Map<String, String> map = userService.register(username, password);
            //包含ticket，说明已注册，需要跳转并保存t
            if(map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);
                //next不为空，跳转到next，因为next是权限请求时的页面
                if(StringUtils.isNotBlank(next)){
                    //判断next的合法性，如输入http://www.baidu.com则跳转回百度,危险
                    if(!Pattern.matches("/user/*", next)){
                        return "redirect:/";
                    }
                    return "redirect:" + next;
                }
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

    //登录注册界面
    @RequestMapping(value = {"/reglogin"}, method = {RequestMethod.GET})
    public String reglogin(Model model, @RequestParam(value = "next", required = false) String next){
        model.addAttribute("next", next);
        return "login";
    }


    //登录功能
    @RequestMapping(value = {"/login", "/login/"})
    public String login(Model model, @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "next",required = false) String next,
                        @RequestParam(value = "remeberme", defaultValue = "false") boolean rememberme,
                        HttpServletResponse response){
        try {
            Map<String, String> map = userService.login(username, password);
            if(map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);
                //next不为空，跳转到next，因为next是权限请求时的页面
                if(StringUtils.isNotBlank(next)){
                    //判断next的合法性，如输入http://www.baidu.com则跳转回百度,危险
                    if(!Pattern.matches("/user/*", next)){
                        return "redirect:/";
                    }
                    return "redirect:" + next;
                }
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

    //登出功能
    @RequestMapping(value = {"/logout", "/logout/"})
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/";//返回首页
    }
}
