package com.ecsimsw;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("/hello")
    public String hello(Model model){
        model.addAttribute("data","hello");
        return "hello";
    }

    @GetMapping("/home")
    public String home(Model model){
        return "home";
    }
}
