package com.tayatu.FreshdeskTicketManagement.dto;

import com.tayatu.FreshdeskTicketManagement.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SenderDTO {

    private String fullName;
    private String email;

    public static SenderDTO fromEntity(User sender) {
        if (sender == null) {
            return null;
        }
        return new SenderDTO(sender.getFullName(), sender.getEmail());
    }
}