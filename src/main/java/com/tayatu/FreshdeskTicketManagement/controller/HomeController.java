package com.tayatu.FreshdeskTicketManagement.controller;

import com.tayatu.FreshdeskTicketManagement.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
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

}
