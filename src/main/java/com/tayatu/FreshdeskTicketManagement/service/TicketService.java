package com.tayatu.FreshdeskTicketManagement.service;

import com.tayatu.FreshdeskTicketManagement.model.Ticket;
import com.tayatu.FreshdeskTicketManagement.model.User;
import com.tayatu.FreshdeskTicketManagement.repository.TicketRepository;
import com.tayatu.FreshdeskTicketManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    public ResponseEntity<List<Ticket>> getAllTickets() {
        Optional<User> agent = userRepository.findByUsername("taya");
        if (agent.isEmpty()) {
            System.out.println("Agent not found " + "taya");
            return ResponseEntity.notFound().build();
        }

        List<Ticket> tickets = ticketRepository.findByAssignedTo(agent.get());
        return ResponseEntity.ok(tickets);
    }

    public ResponseEntity<Ticket> createTicket(Ticket ticket) {
        Optional<User> agentOpt = userRepository.findByUsername("taya");
        Optional<User> raisedByOpt = userRepository.findByUsername("taya");

        if (agentOpt.isEmpty() || raisedByOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        User agent = agentOpt.get();
        User raisedBy = raisedByOpt.get();

        ticket.setAssignedTo(agent);
        ticket.setRaisedBy(raisedBy);
        ticket.setCreatedAt(String.valueOf(java.time.LocalDateTime.now()));
        ticket.setUpdatedAt(String.valueOf(java.time.LocalDateTime.now()));
        Ticket savedTicket = ticketRepository.save(ticket);

        return ResponseEntity.ok(savedTicket);
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

        ticket.setUpdatedAt(String.valueOf(java.time.LocalDateTime.now()));
        ticketRepository.save(ticket);
        return ResponseEntity.ok(ticket);
    }


}
