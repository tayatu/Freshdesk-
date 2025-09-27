package com.tayatu.FreshdeskTicketManagement.controller;

import com.tayatu.FreshdeskTicketManagement.dto.LoginRequest;
import com.tayatu.FreshdeskTicketManagement.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @GetMapping("/home")
    public String home() {
        return "Welcome to the Freshdesk Ticket Management System";
    }

    @PostMapping("/addRoles")
    public String addRole() {
        homeService.addRoles();
        return "Roles Added in DB";
    }

    @PostMapping("/auth/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        return homeService.loginUser(loginRequest);
    }

}
