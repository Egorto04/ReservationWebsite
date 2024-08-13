package com.internproject.internproject.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class NavigationController {

    @RequestMapping("/section1")
    public String showHomePage() {
        return "redirect:/main-page/home";
    }

    @RequestMapping("/section2")
    public String showSection2() {
        return "redirect:/main-page/find-reservation";
    }

    @RequestMapping("/section3")
    public String showSection3() {
        return "redirect:/main-page/section3";
    }

}
