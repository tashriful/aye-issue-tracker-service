package com.aye.issueTracker.service;

import com.aye.issueTracker.model.Department;
import com.aye.issueTracker.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService{

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public Department saveProjects(Department projects) {
        return projectRepository.save(projects);
    }

    @Override
    public List<Department> fetchAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Department findProjectById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteProjectById(Long id) {
        projectRepository.deleteById(id);
    }
}
