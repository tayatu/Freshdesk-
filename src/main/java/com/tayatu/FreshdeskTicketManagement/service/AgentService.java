package com.tayatu.FreshdeskTicketManagement.service;
import com.tayatu.FreshdeskTicketManagement.dto.UserProfileDTO;
import com.tayatu.FreshdeskTicketManagement.model.Ticket;
import com.tayatu.FreshdeskTicketManagement.model.User;
import com.tayatu.FreshdeskTicketManagement.repository.TicketRepository;
import com.tayatu.FreshdeskTicketManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AgentService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private TicketRepository ticketRepository;

    public ResponseEntity<UserProfileDTO> getAgentProfileById(Long id) {
        Optional<User> agent = userRepository.findById(id);
        if (agent.isEmpty()) {
            System.out.println("Agent not found " + id);
            return ResponseEntity.notFound().build();
        }

        User user = agent.get();
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setUsername(user.getUsername());
        userProfileDTO.setEmail(user.getEmail());
        userProfileDTO.setFullName(user.getFullName());
        userProfileDTO.setRole(user.getRole());
        return ResponseEntity.ok(userProfileDTO);
    }

}
