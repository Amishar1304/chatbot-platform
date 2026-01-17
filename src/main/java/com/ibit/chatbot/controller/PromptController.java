package com.ibit.chatbot.controller;

import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibit.chatbot.dto.PromptRequest;
import com.ibit.chatbot.entity.Project;
import com.ibit.chatbot.entity.Prompt;
import com.ibit.chatbot.entity.User;
import com.ibit.chatbot.repository.ProjectRepository;
import com.ibit.chatbot.repository.PromptRepository;

@RestController
@RequestMapping("/projects")
public class PromptController {

    private final ProjectRepository projectRepository;
    private final PromptRepository promptRepository;

    public PromptController(ProjectRepository projectRepository,
                            PromptRepository promptRepository) {
        this.projectRepository = projectRepository;
        this.promptRepository = promptRepository;
    }
    
    @PostMapping("/{projectId}/prompt")
    public Prompt createOrUpdatePrompt(
            @PathVariable Long projectId,
            @RequestBody PromptRequest request,
            @AuthenticationPrincipal User user) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Security check
        if (!project.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        Prompt prompt = promptRepository
                .findByProjectId(projectId)
                .orElse(new Prompt());

        prompt.setContent(request.getContent());
        prompt.setProject(project);

        return promptRepository.save(prompt);
    }
    
    
    @GetMapping("/{projectId}/prompt")
    public Prompt getPrompt(
            @PathVariable Long projectId,
            @AuthenticationPrincipal User user) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!project.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        return promptRepository.findByProjectId(projectId)
                .orElseThrow(() -> new RuntimeException("Prompt not found"));
    }


}
