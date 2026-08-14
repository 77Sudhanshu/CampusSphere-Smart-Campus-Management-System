package com.sudhanshu.smartcampus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        @RequestParam String role) {

        if(role.equals("admin")){
            return "redirect:/admin";
        } else {
            return "redirect:/complaint";
        }
    }
}