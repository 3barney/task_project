package com.test.kcb.taskmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.kcb.taskmanagement.dto.request.CreateProject;
import com.test.kcb.taskmanagement.dto.response.ProjectResponse;
import com.test.kcb.taskmanagement.exception.GlobalExceptionHandler;
import com.test.kcb.taskmanagement.exception.ProjectNotFoundException;
import com.test.kcb.taskmanagement.repository.ProjectRepository;
import com.test.kcb.taskmanagement.repository.TaskRepository;
import com.test.kcb.taskmanagement.service.impl.ProjectServiceImpl;
import com.test.kcb.taskmanagement.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@EnableSpringDataWebSupport
public class ProjectControllerTest {

    @Mock
    private MockMvc mockMvc;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectServiceImpl projectService;

    @InjectMocks
    private ProjectController projectController;

    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(projectController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    public void testCreateProject_Success() throws Exception {
        CreateProject createProject = new CreateProject("New Project", "Project Description");

        ProjectResponse projectResponse = new ProjectResponse(1L, "New Project", "Project Description");

        when(projectService.createProject(any(CreateProject.class))).thenReturn(projectResponse);

        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createProject)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateProject_ValidationFailure() throws Exception {
        CreateProject createProject = new CreateProject("", "Project Description");

        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createProject)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAllProjects_Success() throws Exception {
        List<ProjectResponse> projects = Arrays.asList(
                new ProjectResponse(1L, "Project One", "Description One"),
                new ProjectResponse(2L, "Project Two", "Description Two")
        );

        when(projectService.getAllProjects()).thenReturn(projects);

        mockMvc.perform(get("/projects")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetProjectById_Success() throws Exception {
        Long projectId = 1L;
        ProjectResponse projectResponse = new ProjectResponse(projectId, "Project One", "Description One");

        when(projectService.getProjectById(projectId)).thenReturn(projectResponse);

        mockMvc.perform(get("/projects/{projectId}", projectId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetProjectById_NotFound() throws Exception {
        Long projectId = 1L;

        when(projectService.getProjectById(projectId)).thenThrow(new ProjectNotFoundException("Project not found"));

        mockMvc.perform(get("/projects/{projectId}", projectId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
