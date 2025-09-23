package com.tayatu.FreshdeskTicketManagement.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String subject;
    private String description;
    private String ticketType; // e.g., "Incident", "Service Request"
    private String ticketGroup;      // e.g., "Support", "IT"
    private String status;
    private String priority;
    private String createdAt;
    private String updatedAt;

    @ManyToOne
    @JoinColumn(name = "raised_by_id")
    private User raisedBy;

    @ManyToOne
    @JoinColumn(name = "assigned_to_id")
    private User assignedTo;
}
