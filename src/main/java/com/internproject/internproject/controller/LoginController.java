package com.internproject.internproject.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {


    @GetMapping("/")
    public String showHomePage() {
        return "redirect:/main-page/home";
    }
    @GetMapping("/showLoginPage")
    public String showLoginPage() {
        return "login-page";
    }
}
