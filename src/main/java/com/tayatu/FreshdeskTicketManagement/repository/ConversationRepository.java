package com.tayatu.FreshdeskTicketManagement.repository;

import com.tayatu.FreshdeskTicketManagement.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}
