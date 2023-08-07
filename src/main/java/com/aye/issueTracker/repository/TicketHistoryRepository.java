package com.aye.issueTracker.repository;

import com.aye.issueTracker.model.Employee;
import  com.aye.issueTracker.model.Ticket;
import com.aye.issueTracker.model.TicketHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TicketHistoryRepository extends MongoRepository<TicketHistory, Long> {

    TicketHistory findTicketHistoryByTicketAndAssignedToAndAssignedByAndEndDate
            (Ticket ticket, Employee assignedTo, Employee assignedBy, LocalDate endDate);

    List<TicketHistory> findTicketHistoryByTicket(Ticket ticket);
}
