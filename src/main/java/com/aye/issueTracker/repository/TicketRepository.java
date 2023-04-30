package com.aye.issueTracker.repository;

import com.aye.issueTracker.model.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, Long> {
    List<Ticket> findByDepartment(Long id);

    List<Ticket> findByAssignedTo(Long id);

    List<Ticket> findByTeam(Long id);
}
