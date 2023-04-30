package com.aye.issueTracker.service;

import com.aye.issueTracker.exception.ResourceNotFoundException;
import com.aye.issueTracker.model.Team;
import com.aye.issueTracker.model.TeamHead;
import com.aye.issueTracker.model.User;
import com.aye.issueTracker.repository.TeamHeadRepository;
import com.aye.issuetrackerdto.entityDto.TeamDto;
import com.aye.issuetrackerdto.entityDto.TeamHeadDto;
import com.aye.issuetrackerdto.entityDto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamHeadServiceImpl implements TeamHeadService{

    private final UserService userService;
    private final TeamService teamService;
    private final ModelMapper modelMapper;

    private final TeamHeadRepository teamHeadRepository;

    public TeamHeadServiceImpl(UserService userService, TeamService teamService, ModelMapper modelMapper, TeamHeadRepository teamHeadRepository) {
        this.userService = userService;
        this.teamService = teamService;
        this.modelMapper = modelMapper;
        this.teamHeadRepository = teamHeadRepository;
    }

    @Override
    public TeamHeadDto createTeamHead(TeamHeadDto teamHeadDto) {
        UserDto userDto = userService.getUserById(teamHeadDto.getUserId());
        Optional<TeamDto> teamDto = teamService.getTeamById(teamHeadDto.getTeamId());

        TeamHead teamHead = new TeamHead();

        teamHead.setId(teamHeadDto.getId());
        teamHead.setUser(modelMapper.map(userDto, User.class));
        teamHead.setTeam(modelMapper.map(teamDto.get(), Team.class));

        return this.converToDto(teamHeadRepository.save(teamHead));

    }

    @Override
    public TeamHeadDto updateTeamHead(Long id, TeamHeadDto teamHeadDto) {
        TeamHead teamHead = teamHeadRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid team head id"));

        UserDto userDto = userService.getUserById(teamHeadDto.getUserId());
        teamHead.setUser(modelMapper.map(userDto, User.class));

        TeamDto teamDto = teamService.getTeamById(teamHeadDto.getTeamId()).get();
        teamHead.setTeam(modelMapper.map(teamDto, Team.class));

        TeamHead savedTeamHead = teamHeadRepository.save(teamHead);

        return converToDto(savedTeamHead);
    }

    @Override
    public void deleteTeamHead(Long id) {
        Optional<TeamHead> teamHead = teamHeadRepository.findById(id);
        if(teamHead.isPresent()){
            teamHeadRepository.delete(teamHead.get());
        }
        else{
            throw new ResourceNotFoundException("TeamHead not found for this id: " + id);
        }

    }

    @Override
    public TeamHeadDto getTeamHeadById(Long id) {
        Optional<TeamHead> teamHead = teamHeadRepository.findById(id);
        return teamHead.map(this::converToDto).orElse(null);
    }

    @Override
    public List<TeamHeadDto> getAllTeamHeads() {
        return teamHeadRepository.findAll().stream().map(this::converToDto).collect(Collectors.toList());
    }

    private TeamHeadDto converToDto(TeamHead teamHead){
        return modelMapper.map(teamHead, TeamHeadDto.class);
    }


    private TeamHead convertToEntity(TeamHeadDto teamHeadDto){
        return modelMapper.map(teamHeadDto, TeamHead.class);
    }
}
