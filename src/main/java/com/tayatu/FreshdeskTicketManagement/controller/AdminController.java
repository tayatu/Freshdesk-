package com.tayatu.FreshdeskTicketManagement.controller;

import com.tayatu.FreshdeskTicketManagement.dto.LoginRequest;
import com.tayatu.FreshdeskTicketManagement.dto.UserProfileDTO;
import com.tayatu.FreshdeskTicketManagement.model.User;
import com.tayatu.FreshdeskTicketManagement.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/agents")
    public ResponseEntity<List<UserProfileDTO>> getAllAgents() {
        return adminService.getAllAgents();
    }

    @GetMapping("/agent/{id}")
    public ResponseEntity<UserProfileDTO> getAgentById(@PathVariable Long id) {
        try {
            return adminService.getAgentById(id);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/agent/{id}")
    public ResponseEntity<String> updateAgent(@PathVariable Long id, @RequestBody User agent) {
        try {
            return adminService.updateAgent(id, agent);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
    @DeleteMapping("/agent/{id}")
    public ResponseEntity<String> deleteAgent(@PathVariable Long id) {
        try {
            System.out.println("Deleting agent with id: " + id);
            return adminService.deleteAgent(id);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
    // Get All agents List
    // CRUD Agents
}
