package com.aye.issueTracker.service;

import com.aye.issueTracker.model.Team;
import com.aye.issueTracker.repository.ProjectCorrdinatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectCorrdinatorServiceImpl implements ProjectCorrdinatorService{

    @Autowired
    private ProjectCorrdinatorRepository projectCorrdinatorRepository;

    @Override
    public Team saveProjectCorrdinator(Team projectCorrdinator) {
        return projectCorrdinatorRepository.save(projectCorrdinator);
    }

    @Override
    public List<Team> getAllprojectCorrdinator() {
        return projectCorrdinatorRepository.findAll();
    }

    @Override
    public Team getProjectCoordinatorById(Long id) {
        return projectCorrdinatorRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteProjectCoordinator(Long id) {
        projectCorrdinatorRepository.deleteById(id);
    }
}
