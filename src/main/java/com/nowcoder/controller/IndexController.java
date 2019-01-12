package com.nowcoder.controller;

import com.nowcoder.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class IndexController {

    @RequestMapping(path = {"/","/index"}, method = {RequestMethod.GET, RequestMethod.POST})    //访问的路径
    @ResponseBody   //返回一个文本
    public String index(HttpSession httpSession){
        return "Hello world!" + httpSession.getAttribute("msg");
    }

    @RequestMapping(path = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("userId") int userId,
                          @PathVariable("groupId") String groupId,
                          @RequestParam(value = "type",defaultValue = "0") int type,
                          @RequestParam(value = "key",defaultValue = "none") String key){
        return String.format("Profile Page of %s / %d, t: %d, key : %s", groupId,userId,type,key);
    }

    //返回一个模板
    @RequestMapping(path = {"/vm"})
    public String template(Model model){
        model.addAttribute("value1","dsasd");//从后台传递参数
        List<String> colors = Arrays.asList(new String[]{"RED","BLUE","YELLOW"});
        model.addAttribute("colors",colors);

        Map<String, String> map = new HashMap<>();
        for(int i = 0; i < 4; i++){
            map.put(String.valueOf(i), String.valueOf(i*i));
        }
        model.addAttribute("map",map);
        model.addAttribute("user",new User("XiaoMing"));
        return "home";//返回一个home的模板
    }

    @RequestMapping(path = {"/request"})
    @ResponseBody
    public String request(Model model, HttpServletResponse response,//返回给用户的
                          HttpServletRequest request,//解析自用
                          HttpSession httpSession,
                          @CookieValue("JSESSIONID") String sessionId){//自动解析
        StringBuilder sb = new StringBuilder();
        sb.append("COOKIEVALUE:" + sessionId + "<br>");
        Enumeration<String> headNames = request.getHeaderNames();
        while(headNames.hasMoreElements()){
            String name = headNames.nextElement();
            sb.append(name + " : " +request.getHeader(name) + "<br>");
        }
        if(request.getCookies() != null){
            for(Cookie cookie : request.getCookies()){
                sb.append("cookie name : " + cookie.getName() + " value : " + cookie.getValue() + "<br>");
            }
        }
        sb.append(request.getMethod() + "<br>");
        sb.append(request.getQueryString() + "<br>");
        sb.append(request.getPathInfo() + "<br>");
        sb.append(request.getRequestURI() + "<br>");

        //添加到网页，但是不显示
        response.addHeader("PYH", "hi PYH");
        response.addCookie(new Cookie("username", "pyh"));

        return sb.toString();
    }

    //重定向方法
    @RequestMapping(path = {"/redirect/{code}"}, method = {RequestMethod.GET})
    public RedirectView redirect(@PathVariable("code") int code,
                                 HttpSession httpSession){
        httpSession.setAttribute("msg", "jump from redirect");//设置session跳转
        //return "redirect:/";//重定向到  /  网址下    此时返回值需要为String
        RedirectView red = new RedirectView("/", true); // 指定跳转路径
        if(code == 301){
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return red;
    }

    @RequestMapping(path = {"/admin"}, method = {RequestMethod.GET})
    @ResponseBody
    public String admin(@RequestParam("key") String key){
        if("admin".equals(key)){
            return "hello admin";
        }
        throw new IllegalArgumentException("参数错误"); //可以设置异常界面
    }

    @RequestMapping(path = {"/pyh"})
    @ResponseBody
    public String myMain(HttpSession session){
        return "this is my page!";
    }
}
