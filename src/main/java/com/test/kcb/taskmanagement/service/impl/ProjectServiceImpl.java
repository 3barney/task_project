package com.test.kcb.taskmanagement.service.impl;

import com.test.kcb.taskmanagement.dto.request.CreateProject;
import com.test.kcb.taskmanagement.dto.response.ProjectResponse;
import com.test.kcb.taskmanagement.dto.response.ProjectSummaryResponse;
import com.test.kcb.taskmanagement.entity.Project;
import com.test.kcb.taskmanagement.entity.Task;
import com.test.kcb.taskmanagement.enums.Status;
import com.test.kcb.taskmanagement.exception.ProjectNotFoundException;
import com.test.kcb.taskmanagement.repository.ProjectRepository;
import com.test.kcb.taskmanagement.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectResponse createProject(CreateProject project) {
        Project entity = createProjectEntityFromDto(project);
        Project savedEntity = projectRepository.save(entity);
        ProjectResponse response = createResponseFromEntity(savedEntity);

        return response;
    }

    @Override
    public List<ProjectResponse> getAllProjects() {
        List<Project> projects = projectRepository.findAll();

        List<ProjectResponse> responses = projects.stream()
                .map(this::createResponseFromEntity)
                .collect(Collectors.toList());

        return responses;
    }

    @Override
    public ProjectResponse getProjectById(Long projectId) {
        Optional<Project> projectOpt = projectRepository.findById(projectId);

        if (projectOpt.isPresent()) {
            ProjectResponse response = createResponseFromEntity(projectOpt.get());
            return response;
        } else {
            throw new ProjectNotFoundException("Project not found with ID: " + projectId);
        }
    }

    @Override
    public Void deleteProject(Long projectId) {
        if (projectRepository.existsById(projectId)) {
            projectRepository.deleteById(projectId);
        } else {
            throw new ProjectNotFoundException("Project not found with ID: " + projectId);
        }
        return null;
    }

    @Override
    public List<ProjectSummaryResponse> getProjectSummary() {
        List<Project> projects = projectRepository.findAll();
        List<ProjectSummaryResponse> summaries = new ArrayList<>();

        for (Project project : projects) {
            Map<Status, Long> taskCountByStatus = project.getTasks().stream()
                    .collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));

            ProjectSummaryResponse summaryDTO = new ProjectSummaryResponse();
            summaryDTO.setProjectId(project.getId());
            summaryDTO.setProjectName(project.getName());
            summaryDTO.setTaskCountByStatus(taskCountByStatus);
            summaries.add(summaryDTO);
        }

        return summaries;
    }

    private ProjectResponse createResponseFromEntity(Project entity) {
        return new ProjectResponse(entity.getId(), entity.getName(), entity.getDescription());
    }

    private Project createProjectEntityFromDto(CreateProject dto) {
        Project project = new Project();
        project.setName(dto.name());
        project.setDescription(dto.description());
        return project;
    }
}
