package com.tayatu.FreshdeskTicketManagement.service;

import com.tayatu.FreshdeskTicketManagement.dto.ConversationDTO;
import com.tayatu.FreshdeskTicketManagement.dto.TicketDTO;
import com.tayatu.FreshdeskTicketManagement.enums.TicketPrioirity;
import com.tayatu.FreshdeskTicketManagement.enums.TicketStatus;
import com.tayatu.FreshdeskTicketManagement.model.Conversation;
import com.tayatu.FreshdeskTicketManagement.model.Ticket;
import com.tayatu.FreshdeskTicketManagement.model.User;
import com.tayatu.FreshdeskTicketManagement.repository.ConversationRepository;
import com.tayatu.FreshdeskTicketManagement.repository.TicketRepository;
import com.tayatu.FreshdeskTicketManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private ConversationRepository conversationRepository;

    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            System.out.println("User not found " + username);
            return ResponseEntity.notFound().build();
        }
        List<TicketDTO> ticketDTOs = ticketRepository.findByRequestedId(user.get()).stream()
                .map(TicketDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ticketDTOs);
    }

    public ResponseEntity<TicketDTO> createTicket(Ticket ticket) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> agentOpt = userRepository.findByUsername("officialAgent");
        Optional<User> raisedByOpt = userRepository.findByUsername(username);

        System.out.println("Creating ticket with agent: " + agentOpt);
        System.out.println("Creating ticket with raisedBy: " + raisedByOpt);

        if (agentOpt.isEmpty() || raisedByOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        User agent = agentOpt.get();
        User raisedBy = raisedByOpt.get();

        ticket.setAssignedAgent(agent);
        ticket.setRequestedId(raisedBy);
        ticket.setStatus(ticket.getStatus() == null ? TicketStatus.OPEN : ticket.getStatus());
        ticket.setPriority(ticket.getPriority() == null ? TicketPrioirity.LOW : ticket.getPriority());
        ticket.setCreatedAt(java.time.LocalDateTime.now());
        ticket.setUpdatedAt(java.time.LocalDateTime.now());
        ticket.setConversations(List.of());
        Ticket savedTicket = ticketRepository.save(ticket);

        return ResponseEntity.ok(TicketDTO.fromEntity(savedTicket));
    }

    public ResponseEntity<TicketDTO> getTicketById(Long ticketId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userOpt.get();

        Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);
        if (ticketOpt.isEmpty() || !ticketOpt.get().getRequestedId().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        TicketDTO ticketDTO = TicketDTO.fromEntity(ticketOpt.get());
        return ResponseEntity.ok(ticketDTO);
    }

    public ResponseEntity<ConversationDTO> replyToTicket(Long ticketId, String message) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);
        if (ticketOpt.isEmpty() || !ticketOpt.get().getRequestedId().getId().equals(userOpt.get().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Conversation conversation = new Conversation();

        conversation.setFromCustomer(true);
        conversation.setTicket(ticketOpt.get());
        conversation.setSender(userOpt.get());
        conversation.setMessage(message);
        conversation.setCreatedAt(java.time.LocalDateTime.now());
        conversation.setUpdatedAt(java.time.LocalDateTime.now());

        Conversation savedConversation = conversationRepository.save(conversation);
        ConversationDTO conversationDTO = ConversationDTO.fromEntity(savedConversation);
        return ResponseEntity.ok(conversationDTO);
    }
}
