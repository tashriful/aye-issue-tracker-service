package com.aye.issueTracker.service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.aye.issueTracker.exception.InvalidRequestDataException;
import com.aye.issueTracker.exception.ResourceNotFoundException;
import com.aye.issueTracker.repository.DepartmentRepository;
import com.aye.issuetrackerdto.entityDto.TeamDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.project.issueTracker.exception.ResourceNotFoundException;
import com.aye.issueTracker.model.Team;
import com.aye.issueTracker.repository.TeamRepository;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;
    @Autowired
    private DepartmentRepository departmentRepository;

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

//        Optional<Department> department = departmentRepository.findById(teamDto.getDepartmentId());
//        if (!department.isPresent()) {
//            throw new InvalidRequestDataException("Department Not Exist!");
//        }
    
        Optional<List<Team>> team = teamRepository.findByName(teamDto.getName());
        List<Team> teams = team.get();
        if (!teams.isEmpty()){
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

                Optional<List<Team>> team = teamRepository.findByName(teamDto.getName());
                List<Team> teams = team.get();
                if (!teams.isEmpty()){
                    throw new InvalidRequestDataException("Team Name Already Exist!");
                }

                existingTeam.setName(teamDto.getName());
                existingTeam.setDescription(teamDto.getDescription());
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
            teamRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Team not found for this id: " + id);
        }
    }

    public TeamDto convertToDto(Team team) {
        return modelMapper.map(team, TeamDto.class);
    }

//    public TeamDto convertToDto1(Team team){
//        return new TeamDto(team.getId(), team.getName(), team.getDescription(), team.getDepartment().getId(), team.getDepartment().getName());
////        return modelMapper.map(team, TeamDto.class);
//    }

    public Team convertToEntity(TeamDto teamDto) {
        return modelMapper.map(teamDto, Team.class);
    }

}

