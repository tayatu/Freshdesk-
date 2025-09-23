package com.tayatu.FreshdeskTicketManagement.dto;

import com.tayatu.FreshdeskTicketManagement.model.Ticket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {

    private Long id;
    private String subject;
    private String description;
    private String ticketType;
    private String ticketGroup;
    private String status;
    private String priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private SenderDTO raisedBy;
    private SenderDTO assignedTo;
    private List<ConversationDTO> conversations;


    public static TicketDTO fromEntity(Ticket ticket) {
        if (ticket == null) {
            return null;
        }
        List<ConversationDTO> conversationDTOs = ticket.getConversations().stream()
                .map(ConversationDTO::fromEntity)
                .collect(Collectors.toList());

        return new TicketDTO(
                ticket.getId(),
                ticket.getSubject(),
                ticket.getDescription(),
                ticket.getTicketType(),
                ticket.getTicketGroup(),
                ticket.getStatus(),
                ticket.getPriority(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt(),
                SenderDTO.fromEntity(ticket.getRaisedBy()),
                SenderDTO.fromEntity(ticket.getAssignedTo()),
                conversationDTOs
        );
    }
}
