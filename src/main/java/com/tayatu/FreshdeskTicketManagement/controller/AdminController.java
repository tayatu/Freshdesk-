package com.tayatu.FreshdeskTicketManagement.controller;

import com.tayatu.FreshdeskTicketManagement.dto.LoginRequest;
import com.tayatu.FreshdeskTicketManagement.model.User;
import com.tayatu.FreshdeskTicketManagement.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/create-admin")
    public ResponseEntity<String> createAdmin(@RequestBody LoginRequest loginRequest) {
        try {
            return adminService.createAdmin(loginRequest);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "Admin Dashboard";
    }

    @PostMapping("/create-agent")
    public ResponseEntity<String> createAgent(@RequestBody User agent) {
        try {
            return adminService.createAgent(agent);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    // Get All agents List
    // CRUD Agents
}
