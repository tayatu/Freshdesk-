package com.tayatu.FreshdeskTicketManagement.controller;

import com.tayatu.FreshdeskTicketManagement.dto.ConversationDTO;
import com.tayatu.FreshdeskTicketManagement.dto.TicketDTO;
import com.tayatu.FreshdeskTicketManagement.model.Ticket;
import com.tayatu.FreshdeskTicketManagement.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping
    public ResponseEntity<List<TicketDTO>> viewTickets() {
        return ticketService.getAllTickets();
    }

    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(@RequestBody Ticket newTicket) {
        return ticketService.createTicket(newTicket);
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketDTO> getTicket(@PathVariable Long ticketId) {
        return ticketService.getTicketById(ticketId);
    }

    @PutMapping("/{ticketId}")
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable Long ticketId, @RequestBody Ticket updatedTicket) {
        return ticketService.updateTicket(ticketId, updatedTicket);
    }

    @PostMapping("/{ticketId}/reply")
    public ResponseEntity<ConversationDTO> replyToTicket(
            @PathVariable Long ticketId,
            @RequestBody String message) {

        return ticketService.replyToTicket(ticketId, message);
    }

}
