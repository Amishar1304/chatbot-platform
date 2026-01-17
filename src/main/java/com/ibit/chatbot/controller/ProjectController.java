package com.ibit.chatbot.controller;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.ibit.chatbot.entity.Project;
import com.ibit.chatbot.entity.User;
import com.ibit.chatbot.repository.ProjectRepository;

import java.util.List;
@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    // GET all projects for logged-in user
    @GetMapping
    public List<Project> getProjects(@AuthenticationPrincipal User user) {
        return projectRepository.findByUserId(user.getId());
    }

    // CREATE a new project
    @PostMapping
    public Project createProject(@AuthenticationPrincipal User user,
                                 @RequestBody Project project) {
        project.setUser(user);
        return projectRepository.save(project);
    }
}