package com.lzc.security.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class LoginController {

    @Value("${servername}")
    private String serverName;

    @GetMapping("/authentication/login")
    public String authenticationLogin(Model model) throws IOException {
        System.out.println(serverName);
        model.addAttribute("serverName",serverName);
        return "login";
    }
}
