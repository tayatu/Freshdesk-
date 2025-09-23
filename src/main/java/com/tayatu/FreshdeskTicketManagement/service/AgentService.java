package com.tayatu.FreshdeskTicketManagement.service;
import com.tayatu.FreshdeskTicketManagement.dto.UserProfileDTO;
import com.tayatu.FreshdeskTicketManagement.model.Ticket;
import com.tayatu.FreshdeskTicketManagement.model.User;
import com.tayatu.FreshdeskTicketManagement.repository.TicketRepository;
import com.tayatu.FreshdeskTicketManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AgentService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private TicketRepository ticketRepository;

    public ResponseEntity<UserProfileDTO> getAgentProfileById(Long id) {
        Optional<User> agent = userRepository.findById(id);
        if (agent.isEmpty()) {
            System.out.println("Agent not found " + id);
            return ResponseEntity.notFound().build();
        }

        User user = agent.get();
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setUsername(user.getUsername());
        userProfileDTO.setEmail(user.getEmail());
        userProfileDTO.setFullName(user.getFullName());
        userProfileDTO.setRole(user.getRole());
        return ResponseEntity.ok(userProfileDTO);
    }


    public ResponseEntity<List<Ticket>> getAssignedTickets(Long agentId) {
        Optional<User> agent = userRepository.findById(agentId);
        if (agent.isEmpty()) {
            System.out.println("Agent not found " + agentId);
            return ResponseEntity.notFound().build();
        }

        List<Ticket> tickets = ticketRepository.findByAssignedTo(agent.get());
        return ResponseEntity.ok(tickets);
    }


    public ResponseEntity<Ticket> getTicketById(Long ticketId) {
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);
        return ticket.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Ticket> updateTicket(Long ticketId, Ticket updatedTicket) {
        Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Ticket ticket = ticketOpt.get();
        ticket.setSubject(updatedTicket.getSubject());
        ticket.setDescription(updatedTicket.getDescription());
        ticket.setTicketType(updatedTicket.getTicketType());
        ticket.setTicketGroup(updatedTicket.getTicketGroup());
        ticket.setStatus(updatedTicket.getStatus());
        ticket.setPriority(updatedTicket.getPriority());
        ticket.setUpdatedAt(String.valueOf(java.time.LocalDateTime.now()));

        ticketRepository.save(ticket);
        return ResponseEntity.ok(ticket);
    }

}
