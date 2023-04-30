package com.aye.issueTracker.controller;

import com.aye.issueTracker.common.CustomErrorResponse;
import com.aye.issueTracker.exception.InvalidRequestDataException;
import com.aye.issueTracker.exception.ResourceNotFoundException;
import com.aye.issueTracker.service.SequenceGeneratorService;
import com.aye.issueTracker.service.TeamService;
import com.aye.issuetrackerdto.entityDto.TeamDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @GetMapping
    public List<TeamDto> getAllTeams() {
        return teamService.getAllTeams();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTeamById(@PathVariable("id") Long id) throws ResourceNotFoundException {
        try {
            Optional<TeamDto> teamDto = teamService.getTeamById(id);
            return ResponseEntity.ok().body(teamDto.get());
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND, ZonedDateTime.now()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal Server Error :- " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ZonedDateTime.now()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> createTeam(@RequestBody TeamDto teamDto) {

        try {
            TeamDto savedTeam = teamService.createTeam(teamDto);
            return new ResponseEntity<>(savedTeam, HttpStatus.CREATED);
        } catch (InvalidRequestDataException e) {
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal Server Error :- " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ZonedDateTime.now()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateTeam(@PathVariable("id") Long id, @RequestBody TeamDto teamDto) throws ResourceNotFoundException {
        try {
            TeamDto updatedTeam = teamService.updateTeam(id, teamDto);
            return ResponseEntity.ok().body(updatedTeam);
        } catch (InvalidRequestDataException | ResourceNotFoundException e) {
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal Server Error :- " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ZonedDateTime.now()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTeam(@PathVariable("id") Long id) throws ResourceNotFoundException {
        try {
            teamService.deleteTeamById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND, ZonedDateTime.now()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal Server Error :- " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ZonedDateTime.now()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
