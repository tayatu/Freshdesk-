package com.tayatu.FreshdeskTicketManagement.controller;

import com.tayatu.FreshdeskTicketManagement.dto.ConversationDTO;
import com.tayatu.FreshdeskTicketManagement.dto.TicketDTO;
import com.tayatu.FreshdeskTicketManagement.dto.UserProfileDTO;
import com.tayatu.FreshdeskTicketManagement.model.Ticket;
import com.tayatu.FreshdeskTicketManagement.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/profile/{id}")
    public ResponseEntity<String> updateAgentProfile(@PathVariable Long id, @RequestBody UserProfileDTO profileDTO) {
        System.out.println("Updating profile for agent ID: " + id);
        return agentService.updateAgentProfile(id, profileDTO);
    }

    @DeleteMapping("/profile/{id}")
    public ResponseEntity<String> deleteAgentProfile(@PathVariable Long id) {
        System.out.println("Deleting profile for agent ID: " + id);
        return agentService.deleteAgentProfile(id);
    }

    @GetMapping("/tickets")
    public ResponseEntity<List<TicketDTO>> getAssignedTickets() {
        return agentService.getTicketsAssignedToAgent();
    }

    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<TicketDTO> getTicket(@PathVariable Long ticketId) {
        return agentService.getTicketById(ticketId);
    }

    @PutMapping("/ticket/{ticketId}")
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable Long ticketId, @RequestBody Ticket updatedTicket) {
        return agentService.updateTicket(ticketId, updatedTicket);
    }

    @PostMapping("/ticket/{ticketId}/reply")
    public ResponseEntity<ConversationDTO> replyToTicket(
            @PathVariable Long ticketId,
            @RequestBody String message) {

        return agentService.replyToTicket(ticketId, message);
    }
}
