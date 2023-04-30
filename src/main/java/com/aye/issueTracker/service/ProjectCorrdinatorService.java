package com.aye.issueTracker.service;

import com.aye.issueTracker.model.Team;

import java.util.List;

public interface ProjectCorrdinatorService {

    Team saveProjectCorrdinator(Team projectCorrdinator);

    List<Team> getAllprojectCorrdinator();

    Team getProjectCoordinatorById(Long id);

    void deleteProjectCoordinator(Long id);


}
