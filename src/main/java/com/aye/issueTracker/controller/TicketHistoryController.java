package com.aye.issueTracker.controller;

import com.aye.issueTracker.common.CustomErrorResponse;
import com.aye.issueTracker.exception.InvalidRequestDataException;
import com.aye.issueTracker.exception.ResourceNotFoundException;
import com.aye.issueTracker.service.TicketHistoryService;
import com.aye.issuetrackerdto.entityDto.TeamDto;
import com.aye.issuetrackerdto.entityDto.TicketHistoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ticketHistory")
public class TicketHistoryController {

    @Autowired
    private TicketHistoryService ticketHistoryService;

    @GetMapping("/ticket/{id}")
    public ResponseEntity<?> getTicketHistoryByTicket(@PathVariable("id") Long id){

        try {
            List<TicketHistoryDto> ticketHistoryDtos = ticketHistoryService.getAllTicketHistoryByTicket(id);
            return ResponseEntity.ok().body(ticketHistoryDtos);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND, ZonedDateTime.now()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal Server Error :- " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ZonedDateTime.now()), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
}
