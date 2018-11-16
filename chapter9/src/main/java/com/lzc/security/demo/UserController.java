package com.lzc.security.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/info")
    @ResponseBody
    public Object info(Principal principal) {
        return principal;
    }
}
