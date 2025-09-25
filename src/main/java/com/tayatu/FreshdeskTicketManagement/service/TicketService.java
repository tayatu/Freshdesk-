package com.tayatu.FreshdeskTicketManagement.service;

import com.tayatu.FreshdeskTicketManagement.dto.ConversationDTO;
import com.tayatu.FreshdeskTicketManagement.dto.TicketDTO;
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
        Optional<User> agent = userRepository.findByUsername("taya");
        if (agent.isEmpty()) {
            System.out.println("Agent not found " + "taya");
            return ResponseEntity.notFound().build();
        }

        List<Ticket> tickets = ticketRepository.findByAssignedTo(agent.get());
        List<TicketDTO> ticketDTOs = tickets.stream()
                .map(TicketDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ticketDTOs);
    }

    public ResponseEntity<TicketDTO> createTicket(Ticket ticket) {
        Optional<User> agentOpt = userRepository.findByUsername("taya");
        Optional<User> raisedByOpt = userRepository.findByUsername("taya");

        if (agentOpt.isEmpty() || raisedByOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        User agent = agentOpt.get();
        User raisedBy = raisedByOpt.get();

        ticket.setAssignedTo(agent);
        ticket.setRaisedBy(raisedBy);
        ticket.setCreatedAt(java.time.LocalDateTime.now());
        ticket.setUpdatedAt(java.time.LocalDateTime.now());
        Ticket savedTicket = ticketRepository.save(ticket);

        return ResponseEntity.ok(TicketDTO.fromEntity(savedTicket));
    }

    public ResponseEntity<TicketDTO> getTicketById(Long ticketId) {
        Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        TicketDTO ticketDTO = TicketDTO.fromEntity(ticketOpt.get());
        return ResponseEntity.ok(ticketDTO);
    }

    public ResponseEntity<TicketDTO> updateTicket(Long ticketId, Ticket updatedTicket) {
        Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Ticket ticket = ticketOpt.get();

        if (updatedTicket.getSubject() != null) {
            ticket.setSubject(updatedTicket.getSubject());
        }
        if (updatedTicket.getDescription() != null) {
            ticket.setDescription(updatedTicket.getDescription());
        }
        if (updatedTicket.getTicketType() != null) {
            ticket.setTicketType(updatedTicket.getTicketType());
        }
        if (updatedTicket.getTicketGroup() != null) {
            ticket.setTicketGroup(updatedTicket.getTicketGroup());
        }
        if (updatedTicket.getStatus() != null) {
            ticket.setStatus(updatedTicket.getStatus());
        }
        if (updatedTicket.getPriority() != null) {
            ticket.setPriority(updatedTicket.getPriority());
        }

        ticket.setUpdatedAt(java.time.LocalDateTime.now());
        ticketRepository.save(ticket);

        return ResponseEntity.ok(TicketDTO.fromEntity(ticket));
    }

    public ResponseEntity<ConversationDTO> replyToTicket(Long ticketId, String message) {
        Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);
        Optional<User> userOpt = userRepository.findByUsername("taya");

        if (ticketOpt.isEmpty() || userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Conversation conversation = new Conversation();
        conversation.setTicket(ticketOpt.get());
        conversation.setSender(userOpt.get());
        conversation.setMessage(message);
        conversation.setSentAt(java.time.LocalDateTime.now());

        Conversation savedConversation = conversationRepository.save(conversation);
        ConversationDTO conversationDTO = ConversationDTO.fromEntity(savedConversation);
        return ResponseEntity.ok(conversationDTO);
    }
}
