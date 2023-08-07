package com.aye.issueTracker.service;

import com.aye.issueTracker.model.Employee;
import com.aye.issuetrackerdto.entityDto.TicketDto;
import com.aye.issuetrackerdto.entityDto.TicketHistoryDto;

import java.util.List;

public interface TicketHistoryService {

    TicketHistoryDto saveTicketHistory(TicketDto ticketHistoryDto);
    List<TicketHistoryDto> getAllTicketHistory();
    TicketHistoryDto getTicketHistoryById(Long id);
    void deleteTicketHistory(Long id);


    void updateTicketHistoryEndDate(Employee prevAssignedToUser, Employee prevAssignedByUser, Long ticketId);

    List<TicketHistoryDto> getAllTicketHistoryByTicket(Long id);
}
