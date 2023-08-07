package com.aye.issueTracker.service;

import com.aye.issuetrackerdto.entityDto.TicketDto;

import java.io.IOException;
import java.util.List;

public interface TicketService {

    TicketDto saveTicket(TicketDto ticketDto);

    List<TicketDto> getAllTicket();

    TicketDto getTicketById(Long id) throws IOException;

    void deleteTicket(Long id);

    List<TicketDto> getTicketByDepartment(Long userId);

    List<TicketDto> getTicketByAssignedTo(Long id);

    List<TicketDto> getTicketByTeam(Long id) throws IOException;

    TicketDto updateTicketStatus(Long id, TicketDto ticketDto);

    TicketDto updateAssignedTo(Long id, TicketDto ticketDto);

    TicketDto updateAssignedToTicketHistory(Long id, TicketDto ticketDto);

    List<TicketDto> getTicketsByCreatedBy(Long id);

    TicketDto updateTicket(Long ticketId, TicketDto ticketDto);
}
