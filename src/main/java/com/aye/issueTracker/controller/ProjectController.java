package com.aye.issueTracker.controller;

import com.aye.issueTracker.model.Department;
import com.aye.issueTracker.service.ProjectService;
import com.aye.issueTracker.service.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @PostMapping("/")
    private ResponseEntity<Department> saveProject(@RequestBody Department project){
        project.setId(sequenceGeneratorService.generateSequence(Department.SEQUENCE_NAME));
        Department projects =  projectService.saveProjects(project);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/")
    private List<Department> getAllProject(){
        return projectService.fetchAllProjects();
    }

    @GetMapping("/{id}")
    private Department getProjectById(@PathVariable("id") Long id){
        return projectService.findProjectById(id);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<HttpStatus> deleteProjectById(@PathVariable("id") Long id){
        projectService.deleteProjectById(id);
         return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }



}
