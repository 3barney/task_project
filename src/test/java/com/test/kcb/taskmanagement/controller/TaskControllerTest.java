package com.test.kcb.taskmanagement.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.test.kcb.taskmanagement.dto.request.CreateTask;
import com.test.kcb.taskmanagement.dto.response.TaskResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.test.kcb.taskmanagement.entity.Project;
import com.test.kcb.taskmanagement.enums.Status;
import com.test.kcb.taskmanagement.exception.GlobalExceptionHandler;
import com.test.kcb.taskmanagement.repository.ProjectRepository;
import com.test.kcb.taskmanagement.repository.TaskRepository;
import com.test.kcb.taskmanagement.service.TaskService;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@EnableSpringDataWebSupport
public class TaskControllerTest {

    @Mock
    private MockMvc mockMvc;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskServiceImpl taskService;

    @InjectMocks
    private TaskController taskController;


    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(taskController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    public void testAddTask_Success() throws Exception {
        Long projectId = 1L;
        CreateTask createTask = new CreateTask("New Task", "Task Description", Status.TO_DO, LocalDate.now().plusDays(7), projectId);

        TaskResponse taskResponse = new TaskResponse(1L, "New Task", "Task Description",
                Status.TO_DO, createTask.dueDate(), projectId);

        var project = new Project();
        project.setId(projectId);

        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));
        when(taskService.addTaskToProject(anyLong(), any(CreateTask.class))).thenReturn(taskResponse);

        mockMvc.perform(post("/projects/{projectId}/tasks", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createTask)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetTasks_Success() throws Exception {
        Long projectId = 1L;
        List<TaskResponse> tasks = Arrays.asList(
                new TaskResponse(1L, "Task One", "Description One", Status.TO_DO, LocalDate.now(), projectId),
                new TaskResponse(2L, "Task Two", "Description Two", Status.IN_PROGRESS, LocalDate.now().plusDays(1), projectId)
        );

        when(taskService.getTasksByProject(anyLong(), any(), any(), any())).thenReturn(tasks);

        mockMvc.perform(get("/projects/{projectId}/tasks", projectId)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateTask_Success() throws Exception {
        Long projectId = 1L;
        Long taskId = 1L;
        CreateTask updateTask = new CreateTask("Updated Task", "Updated Description", Status.DONE, LocalDate.now().plusDays(10), projectId);

        TaskResponse updatedTaskResponse = new TaskResponse(taskId, updateTask.title(), updateTask.description(),
                updateTask.status(), updateTask.dueDate(), projectId);

        when(taskService.updateTask(eq(taskId), any(CreateTask.class))).thenReturn(updatedTaskResponse);

        mockMvc.perform(put("/projects/{projectId}/tasks/{taskId}", projectId, taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateTask)))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteTask_Success() throws Exception {
        Long projectId = 1L;
        Long taskId = 1L;

        Mockito.doNothing().when(taskService).deleteTask(taskId);

        mockMvc.perform(delete("/projects/{projectId}/tasks/{taskId}", projectId, taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
