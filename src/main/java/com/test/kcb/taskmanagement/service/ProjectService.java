package com.test.kcb.taskmanagement.service;

import com.test.kcb.taskmanagement.dto.request.CreateProject;
import com.test.kcb.taskmanagement.dto.response.ProjectResponse;
import com.test.kcb.taskmanagement.dto.response.ProjectSummaryResponse;

import java.util.List;

public interface ProjectService {

    ProjectResponse createProject(CreateProject project);

    List<ProjectResponse> getAllProjects();

    ProjectResponse getProjectById(Long projectId);

    Void deleteProject(Long projectId);

    List<ProjectSummaryResponse> getProjectSummary();
}
