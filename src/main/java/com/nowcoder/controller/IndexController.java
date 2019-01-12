package com.nowcoder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @RequestMapping(path = {"/","/index"})    //默认访问/
    @ResponseBody   //返回一个字符串，所以用Body
    public String index(){
        return "Hello world!";
    }



}
