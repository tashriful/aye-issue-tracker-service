package com.aye.issueTracker.service;

import com.aye.issueTracker.exception.ResourceNotFoundException;
import com.aye.issuetrackerdto.entityDto.TeamDto;

import java.util.List;
import java.util.Optional;

public interface TeamService {

    List<TeamDto> getAllTeams();

    Optional<TeamDto> getTeamById(Long id);

    TeamDto createTeam(TeamDto teamDto);

    TeamDto updateTeam(Long id, TeamDto teamDto) throws ResourceNotFoundException;

    void deleteTeamById(Long id) throws ResourceNotFoundException;
}
