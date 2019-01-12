package com.nowcoder.controller;

import com.nowcoder.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

}
