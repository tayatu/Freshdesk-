package com.tayatu.FreshdeskTicketManagement.repository;

import com.tayatu.FreshdeskTicketManagement.model.Ticket;
import com.tayatu.FreshdeskTicketManagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    long countByAssignedAgentId(Long id);

    List<Ticket> findByRequestedId(User user);

    List<Ticket> findByAssignedAgentId(Long id);

}
