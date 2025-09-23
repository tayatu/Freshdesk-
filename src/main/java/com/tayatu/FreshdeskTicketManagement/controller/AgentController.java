package com.tayatu.FreshdeskTicketManagement.controller;
import com.tayatu.FreshdeskTicketManagement.dto.UserProfileDTO;
import com.tayatu.FreshdeskTicketManagement.model.Ticket;
import com.tayatu.FreshdeskTicketManagement.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agent")
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

    @GetMapping("/tickets")
    public ResponseEntity<List<Ticket>> viewTickets(@PathVariable Long id) {
        return agentService.getAssignedTickets(id);
    }

    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<Ticket> getTicket(@PathVariable Long ticketId) {
        return agentService.getTicketById(ticketId);
    }

    @PutMapping("/ticket/{ticketId}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Long ticketId, @RequestBody Ticket updatedTicket) {
        return agentService.updateTicket(ticketId, updatedTicket);
    }

}
