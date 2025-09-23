package com.tayatu.FreshdeskTicketManagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agent")
public class AgentController {
    @GetMapping("/dashboard")
    public String agentDashboard() {
        return "Agent Dashboard";
    }
}
