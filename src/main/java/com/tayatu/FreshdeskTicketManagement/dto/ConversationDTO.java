package com.tayatu.FreshdeskTicketManagement.dto;

import com.tayatu.FreshdeskTicketManagement.model.Conversation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationDTO {

    private Long id;

    private Boolean fromCustomer;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String message;
    private Long ticketId;
    private Long senderId;

    public static ConversationDTO fromEntity(Conversation conversation) {
        if (conversation == null) {
            return null;
        }
        return new ConversationDTO(
                conversation.getId(),
                conversation.getFromCustomer(),
                conversation.getCreatedAt(),
                conversation.getUpdatedAt(),
                conversation.getMessage(),
                conversation.getTicket() != null ? conversation.getTicket().getId() : null,
                conversation.getSender() != null ? conversation.getSender().getId() : null
        );
    }
}