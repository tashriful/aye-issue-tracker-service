package com.aye.issueTracker.service;

import com.aye.issuetrackerdto.entityDto.TeamHeadDto;

import java.util.List;

public interface TeamHeadService {

    TeamHeadDto createTeamHead(TeamHeadDto teamHeadDto);
    TeamHeadDto updateTeamHead(Long id, TeamHeadDto teamHeadDto);
    void deleteTeamHead(Long id);
    TeamHeadDto getTeamHeadById(Long id);
    List<TeamHeadDto> getAllTeamHeads();
}
