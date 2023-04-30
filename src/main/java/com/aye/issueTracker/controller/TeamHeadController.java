package com.aye.issueTracker.controller;

import com.aye.issueTracker.model.TeamHead;
import com.aye.issueTracker.service.SequenceGeneratorService;
import com.aye.issueTracker.service.TeamHeadService;
import com.aye.issuetrackerdto.entityDto.TeamHeadDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teamheads")
public class TeamHeadController {
    @Autowired
    private TeamHeadService teamHeadService;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @PostMapping
    public TeamHeadDto createTeamHead(@RequestBody TeamHeadDto teamHeadDTO) {
        teamHeadDTO.setId(sequenceGeneratorService.generateSequence(TeamHead.SEQUENCE_NAME));
        return teamHeadService.createTeamHead(teamHeadDTO);
    }

    @GetMapping("/{id}")
    public TeamHeadDto getTeamHeadById(@PathVariable Long id) {
        return teamHeadService.getTeamHeadById(id);
    }

    @GetMapping
    public List<TeamHeadDto> getAllTeamHeads() {
        return teamHeadService.getAllTeamHeads();
    }

    @PutMapping("/{id}")
    public TeamHeadDto updateTeamHead(@PathVariable Long id, @RequestBody TeamHeadDto teamHeadDTO) {
        return teamHeadService.updateTeamHead(id, teamHeadDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteTeamHead(@PathVariable Long id) {
        teamHeadService.deleteTeamHead(id);
    }
}

