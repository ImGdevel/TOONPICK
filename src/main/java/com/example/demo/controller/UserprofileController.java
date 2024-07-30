package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class UserprofileController {
    @GetMapping("/my")
    public String myPage() {

        return "userprofile";
    }
}
