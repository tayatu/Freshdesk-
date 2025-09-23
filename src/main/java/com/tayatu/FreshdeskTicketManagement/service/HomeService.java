package com.tayatu.FreshdeskTicketManagement.service;

import com.tayatu.FreshdeskTicketManagement.model.Ticket;
import com.tayatu.FreshdeskTicketManagement.model.User;
import com.tayatu.FreshdeskTicketManagement.repository.TicketRepository;
import com.tayatu.FreshdeskTicketManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HomeService {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

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
}
