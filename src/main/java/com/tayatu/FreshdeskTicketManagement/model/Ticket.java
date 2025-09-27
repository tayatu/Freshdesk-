package com.tayatu.FreshdeskTicketManagement.model;

import com.tayatu.FreshdeskTicketManagement.enums.TicketPrioirity;
import com.tayatu.FreshdeskTicketManagement.enums.TicketStatus;
import com.tayatu.FreshdeskTicketManagement.enums.TicketType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String subject;
    private String description;
    private TicketType ticketType;
    private String ticketGroup;
    private TicketStatus status;
    private TicketPrioirity priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @ManyToOne
    @JoinColumn(name = "raised_by_id")
    private User requestedId;

    @ManyToOne
    @JoinColumn(name = "assigned_to_id")
    private User assignedAgent;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<Conversation> conversations;
}
