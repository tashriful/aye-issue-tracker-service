package com.aye.issueTracker.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.aye.issueTracker.exception.InvalidRequestDataException;
import com.aye.issueTracker.exception.NotDeletableException;
import com.aye.issueTracker.exception.ResourceNotFoundException;
import com.aye.issueTracker.repository.*;
import com.aye.issuetrackerdto.entityDto.TeamDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

//import com.project.issueTracker.exception.ResourceNotFoundException;
import com.aye.issueTracker.model.Team;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<TeamDto> getAllTeams() {
        List<Team> teams = teamRepository.findAll();
        return teams.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public Optional<TeamDto> getTeamById(Long id) {
        Optional<Team> team = teamRepository.findById(id);
        if(team.isPresent()){
            return team.map(this::convertToDto);
        }
        else{
            throw new ResourceNotFoundException("Team not found with this id: "+id);
        }

    }

    @Override
    public TeamDto createTeam(TeamDto teamDto) {


        validateDto(teamDto);

        Team team = new Team();
        team.setId(sequenceGeneratorService.generateSequence(Team.SEQUENCE_NAME));
        team.setName(teamDto.getName());
        team.setDescription(teamDto.getDescription());
        String currentUserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentUserId = userRepository.findByUsername(currentUserName).get().getId();
        team.setCreatedById(currentUserId);
        team.setCreatedDateTime(LocalDateTime.now());
        return this.convertToDto(teamRepository.save(team));
    }

    private void validateDto(TeamDto teamDto) throws InvalidRequestDataException {

        if (teamDto == null) {
            throw new InvalidRequestDataException("Invalid Object");
        }

        if (teamDto.getName() == null || teamDto.getName().isEmpty()) {
            throw new InvalidRequestDataException("Name Can't be empty");
        }

        if (teamDto.getDescription() == null || teamDto.getDescription().isEmpty()) {
            throw new InvalidRequestDataException("Description Can't Be empty");
        }

    
        Optional<Team> team = teamRepository.findByName(teamDto.getName());

        if (team.isPresent()){
            throw new InvalidRequestDataException("Team Name Already Exist!");
        }

    }

    @Override
    public TeamDto updateTeam(Long id, TeamDto teamDto) throws ResourceNotFoundException {

//        validateDto(teamDto);

        Optional<Team> optionalTeam = teamRepository.findById(id);
        if (optionalTeam.isPresent()) {
            Team existingTeam = optionalTeam.get();
            if(teamDto.getName() != null){

                Optional<Team> team = teamRepository.findByName(teamDto.getName());
                if (team.isPresent()){
                    if (team.get().getId() != teamDto.getId()) {
                        throw new InvalidRequestDataException("Team Name Already Exist!");
                    }
                }

                existingTeam.setName(teamDto.getName());
                existingTeam.setDescription(teamDto.getDescription());
                String currentUserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Long currentUserId = userRepository.findByUsername(currentUserName).get().getId();
                existingTeam.setUpdatedById(currentUserId);
                existingTeam.setUpdatedDateTime(LocalDateTime.now());
                Team savedTeam = teamRepository.save(existingTeam);
                return convertToDto(savedTeam);
            }
            else {
                existingTeam.setDescription(teamDto.getDescription());
                Team savedTeam = teamRepository.save(existingTeam);
                return convertToDto(savedTeam);
            }
        } else {
            throw new ResourceNotFoundException("Team not found for this id: " + id);
        }
    }

    @Override
    public void deleteTeamById(Long id) throws ResourceNotFoundException {
        Optional<Team> optionalTeam = teamRepository.findById(id);
        if (optionalTeam.isPresent()) {

            Boolean employeeExist = employeeRepository.existsByTeam(optionalTeam.get());
            Boolean ticketExist = ticketRepository.existsByTeam(optionalTeam.get());

            if (employeeExist || ticketExist) {
                throw new NotDeletableException("Delete operation not possible! Associate Entity Exist!");
            }

            teamRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Team not found for this id: " + id);
        }
    }

    public TeamDto convertToDto(Team team) {
        return modelMapper.map(team, TeamDto.class);
    }


    public Team convertToEntity(TeamDto teamDto) {
        return modelMapper.map(teamDto, Team.class);
    }

}

