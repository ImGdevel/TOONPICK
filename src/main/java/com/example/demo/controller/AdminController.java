package com.example.demo.controller;


import com.example.demo.dto.JoinDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Console;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @PostMapping()
    public String joinProcess(JoinDTO joinDTO) {
        System.out.println("Admin");
        return "Admin";
    }

}
