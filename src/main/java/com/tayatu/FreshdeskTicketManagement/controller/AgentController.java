package com.tayatu.FreshdeskTicketManagement.controller;

import com.tayatu.FreshdeskTicketManagement.dto.UserProfileDTO;
import com.tayatu.FreshdeskTicketManagement.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agent")
@PreAuthorize("hasRole('ROLE_AGENT')")
public class AgentController {

    @Autowired
    private AgentService agentService;

    @GetMapping("/dashboard")
    public String agentDashboard() {
        return "Agent Dashboard";
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<UserProfileDTO> agentProfile(@PathVariable Long id) {
        System.out.println("Fetching profile for agent ID: " + id);
        return agentService.getAgentProfileById(id);
    }

    // Crud Agent. update name number etc
}
