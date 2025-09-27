package com.tayatu.FreshdeskTicketManagement.service;

import com.tayatu.FreshdeskTicketManagement.dto.ConversationDTO;
import com.tayatu.FreshdeskTicketManagement.dto.TicketDTO;
import com.tayatu.FreshdeskTicketManagement.dto.UserProfileDTO;
import com.tayatu.FreshdeskTicketManagement.model.Conversation;
import com.tayatu.FreshdeskTicketManagement.model.Ticket;
import com.tayatu.FreshdeskTicketManagement.model.User;
import com.tayatu.FreshdeskTicketManagement.repository.TicketRepository;
import com.tayatu.FreshdeskTicketManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("Authenticated username: " + username);
        if (agent.isEmpty()) {
            System.out.println("Agent not found " + id);
            return ResponseEntity.notFound().build();
        }

        User user = agent.get();
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setUsername(user.getUsername());
        userProfileDTO.setEmail(user.getEmail());
        userProfileDTO.setFullName(user.getFullName());
        userProfileDTO.setRoles(user.getRoles());
        return ResponseEntity.ok(userProfileDTO);
    }

    public ResponseEntity<String> updateAgentProfile(Long id, UserProfileDTO profileDTO) {
        // verify that the authenticated user is the same as the agent being updated
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User authUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
        if (!authUser.getId().equals(id)) {
            return ResponseEntity.status(403).body("You can only update your own profile");
        }

        Optional<User> agentOpt = userRepository.findById(id);
        if (agentOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User agent = agentOpt.get();
        agent.setEmail(profileDTO.getEmail());
        agent.setFullName(profileDTO.getFullName());
        agent.setPhoneNumber(profileDTO.getPhoneNumber());

        userRepository.save(agent);
        return ResponseEntity.ok("Profile updated successfully");
    }

    public ResponseEntity<String> deleteAgentProfile(Long id) {
        // verify that the authenticated user is the same as the agent being deleted
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User authUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
        if (!authUser.getId().equals(id)) {
            return ResponseEntity.status(403).body("You can only delete your own profile");
        }

        Optional<User> agentOpt = userRepository.findById(id);
        if (agentOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Check if the agent has any assigned tickets
        long assignedTicketCount = ticketRepository.countByAssignedAgentId(id);
        if (assignedTicketCount > 0) {
            return ResponseEntity.status(400).body("Cannot delete profile. You have assigned tickets.");
        }

        userRepository.deleteById(id);
        return ResponseEntity.ok("Profile deleted successfully");
    }

    public ResponseEntity<TicketDTO> getTicketById(Long ticketId) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userOpt.get();

        Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);
        if (ticketOpt.isEmpty() || !ticketOpt.get().getAssignedAgent().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        TicketDTO ticketDTO = TicketDTO.fromEntity(ticketOpt.get());
        return ResponseEntity.ok(ticketDTO);
    }

    public ResponseEntity<TicketDTO> updateTicket(Long ticketId, Ticket updatedTicket) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userOpt.get();

        Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);
        if (ticketOpt.isEmpty() || !ticketOpt.get().getAssignedAgent().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Ticket ticket = ticketOpt.get();
        if (updatedTicket.getStatus() != null) {
            ticket.setStatus(updatedTicket.getStatus());
        }
        if (updatedTicket.getPriority() != null) {
            ticket.setPriority(updatedTicket.getPriority());
        }
        ticketRepository.save(ticket);
        TicketDTO ticketDTO = TicketDTO.fromEntity(ticket);
        return ResponseEntity.ok(ticketDTO);
    }

    public ResponseEntity<ConversationDTO> replyToTicket(Long ticketId, String message) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userOpt.get();

        Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);
        if (ticketOpt.isEmpty() || !ticketOpt.get().getAssignedAgent().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Ticket ticket = ticketOpt.get();

        Conversation agentReply = Conversation.builder()
                .message(message)
                .sender(user)
                .ticket(ticket)
                .fromCustomer(false)
                .createdAt(java.time.LocalDateTime.now())
                .updatedAt(java.time.LocalDateTime.now())
                .build();

        ticket.getConversations().add(agentReply);
        ticketRepository.save(ticket);
        ConversationDTO conversationDTO = ConversationDTO.fromEntity(agentReply);
        return ResponseEntity.ok(conversationDTO);
    }

    public ResponseEntity<List<TicketDTO>> getTicketsAssignedToAgent() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userOpt.get();

        List<Ticket> tickets = ticketRepository.findByAssignedAgentId(user.getId());
        List<TicketDTO> ticketDTOs = tickets.stream()
                .map(TicketDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(ticketDTOs);
    }
}
