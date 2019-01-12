package com.nowcoder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class IndexController {

    @RequestMapping(path = {"/","/index"}, method = {RequestMethod.GET, RequestMethod.POST})    //访问的路径
    @ResponseBody   //返回一个页面
    public String index(){
        return "Hello world!";
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
    public String template(){
        return "home";//返回一个home的模板
    }

}
