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

    private String message;
    private LocalDateTime sentAt;
    private SenderDTO sender;

    public static ConversationDTO fromEntity(Conversation conversation) {
        if (conversation == null) {
            return null;
        }
        return new ConversationDTO(
                conversation.getMessage(),
                conversation.getSentAt(),
                SenderDTO.fromEntity(conversation.getSender())
        );
    }
}