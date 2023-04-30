package com.aye.issueTracker.service;

import com.aye.issueTracker.exception.ResourceNotFoundException;
import com.aye.issueTracker.model.Employee;
import com.aye.issueTracker.model.Ticket;
import com.aye.issueTracker.model.TicketHistory;
import com.aye.issueTracker.repository.EmployeeRepository;
import com.aye.issueTracker.repository.TicketHistoryRepository;
import com.aye.issueTracker.repository.TicketRepository;
import com.aye.issuetrackerdto.entityDto.TicketDto;
import com.aye.issuetrackerdto.entityDto.TicketHistoryDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TicketHistoryImpl implements TicketHistoryService{

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TicketHistoryRepository ticketHistoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Override
    public TicketHistoryDto saveTicketHistory(TicketDto ticketDto) {
        Ticket ticket = ticketRepository.findById(ticketDto.getId()).orElse(null);
        Employee assignedBy = employeeRepository.findById(ticketDto.getAssignedById()).orElse(null);
        Employee assignedTo = employeeRepository.findById(ticketDto.getAssignedToUser()).orElse(null);
        if (ticket == null || assignedBy == null || assignedTo == null) {
            return null;
        }
        TicketHistory ticketHistory = new TicketHistory();
        ticketHistory.setId(sequenceGeneratorService.generateSequence(TicketHistory.SEQUENCE_NAME));
        ticketHistory.setTicket(ticket);
        ticketHistory.setAssignedBy(assignedBy);
        ticketHistory.setAssignedTo(assignedTo);
        ticketHistory.setStartDate(LocalDate.now());
        LocalDate endDate = null;
        ticketHistory.setEndDate(endDate);
        ticketHistory.setPrevStatus(ticketDto.getStatus());

        TicketHistory createdTicketHistory = ticketHistoryRepository.save(ticketHistory);
        return converToDto(ticketHistory);

    }

    @Override
    public List<TicketHistoryDto> getAllTicketHistory() {
        return null;
    }

    @Override
    public TicketHistoryDto getTicketHistoryById(Long id) {
        Optional<TicketHistory> ticketHistory = ticketHistoryRepository.findById(id);
        if(ticketHistory.isPresent()){
            return converToDto(ticketHistory.get());
        }
        else {
            throw new ResourceNotFoundException("TicketHistory Not found with this id: "+id);
        }
    }

    @Override
    public void updateTicketHistoryEndDate(Employee prevAssignedToUser, Employee prevAssignedByUser, Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElse(null);
        TicketHistory ticketHistory = ticketHistoryRepository.
                findTicketHistoryByTicketAndAssignedToAndAssignedByAndEndDate(ticket, prevAssignedToUser, prevAssignedByUser, null);

        ticketHistory.setEndDate(LocalDate.now());
        ticketHistoryRepository.save(ticketHistory);
    }

    @Override
    public void deleteTicketHistory(Long id) {

    }

    private TicketHistoryDto converToDto(TicketHistory ticketHistory){
        return modelMapper.map(ticketHistory, TicketHistoryDto.class);
    }


    private TicketHistory convertToEntity(TicketHistoryDto ticketHistoryDto){
        return modelMapper.map(ticketHistoryDto, TicketHistory.class);
    }
}
