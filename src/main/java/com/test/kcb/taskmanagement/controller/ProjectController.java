package com.test.kcb.taskmanagement.controller;

import com.test.kcb.taskmanagement.dto.request.CreateProject;
import com.test.kcb.taskmanagement.dto.response.ProjectResponse;
import com.test.kcb.taskmanagement.dto.response.ProjectSummaryResponse;
import com.test.kcb.taskmanagement.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/projects", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody CreateProject project) {
        ProjectResponse createdProject = projectService.createProject(project);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        List<ProjectResponse> projects = projectService.getAllProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long projectId) {
        ProjectResponse project = projectService.getProjectById(projectId);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @GetMapping("/summary")
    public ResponseEntity<List<ProjectSummaryResponse>> getProjectSummary() {
        List<ProjectSummaryResponse> summary = projectService.getProjectSummary();
        return new ResponseEntity<>(summary, HttpStatus.OK);
    }
}
