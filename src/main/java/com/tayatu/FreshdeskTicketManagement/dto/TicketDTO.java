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
    private Long requestedId;
    private Long assignedAgentId;
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
                String.valueOf(ticket.getTicketType()),
                ticket.getTicketGroup(),
                String.valueOf(ticket.getStatus()),
                String.valueOf(ticket.getPriority()),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt(),
                ticket.getRequestedId().getId(),
                ticket.getAssignedAgent().getId(),
                conversationDTOs
        );
    }
}
