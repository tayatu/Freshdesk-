package com.tayatu.FreshdeskTicketManagement.controller;

import com.tayatu.FreshdeskTicketManagement.dto.LoginRequest;
import com.tayatu.FreshdeskTicketManagement.model.User;
import com.tayatu.FreshdeskTicketManagement.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<String> adminLogin(@RequestBody LoginRequest loginRequest) {
        return adminService.addUser(loginRequest);
    }

    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "Admin Dashboard";
    }

}
