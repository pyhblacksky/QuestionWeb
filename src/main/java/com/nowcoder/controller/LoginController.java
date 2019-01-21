package com.nowcoder.controller;

import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventProducer;
import com.nowcoder.async.EventType;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;

    //注册功能
    @RequestMapping(value = {"/reg/","/reg"})
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "next",required = false) String next,
                      @RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme,
                      HttpServletResponse response){
        try {
            Map<String, Object> map = userService.register(username, password);
            //包含ticket，说明已注册，需要跳转并保存t
            if(map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                response.addCookie(cookie);
                //next不为空，跳转到next，因为next是权限请求时的页面
                if(StringUtils.isNotBlank(next)){
                    //判断next的合法性，如输入http://www.baidu.com则跳转回百度,危险
                    if(!Pattern.matches("/user/[0-9]*", next)){
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

    //登录注册界面，此时可以跳转，但是单纯登录注册不能访问，服务器会报错.
    //原因：如果设置为false时，当请求中没有此参数，将会默认为null,而对于基本数据类型的变量，
    //          则必须有值，这时会抛出空指针异常。如果允许空值，则接口中变量需要使用包装类来声明。
    //  defaultValue：参数的默认值，如果请求中没有同名的参数时，该变量默认为此值。
    @RequestMapping(value = {"/reglogin"}, method = {RequestMethod.GET})
    public String reglogin(Model model,@RequestParam(value = "next", required = false, defaultValue = "") String next) {
        if(next != null && next.length() != 0){
            model.addAttribute("next", next);
        }
        return "login";
    }


    //登录功能
    @RequestMapping(value = {"/login", "/login/"}, method = {RequestMethod.POST})
    public String login(Model model, @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "next",required = false) String next,
                        @RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme,
                        HttpServletResponse response){
        try {
            Map<String, Object> map = userService.login(username, password);
            if(map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                response.addCookie(cookie);

                //进行登录事件处理,判断有无异常
                eventProducer.fireEvent(new EventModel(EventType.LOGIN)
                        .setExts("username", username).setExts("email", "xxx@qq.com")//填写投递邮箱
                        .setActorId((int)map.get("userId")));

                //next不为空，跳转到next，因为next是权限请求时的页面
                if(StringUtils.isNotBlank(next)){
                    //判断next的合法性，如输入http://www.baidu.com则跳转回百度,危险
                    System.out.println(next);
                    if(!Pattern.matches("/user/[1-9]*", next)){
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
            logger.error("登录异常 ： " + e.getMessage());
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
