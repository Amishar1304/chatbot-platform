package com.ibit.chatbot.controller;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ibit.chatbot.entity.Project;
import com.ibit.chatbot.entity.ProjectFile;
import com.ibit.chatbot.entity.User;
import com.ibit.chatbot.repository.ProjectFileRepository;
import com.ibit.chatbot.repository.ProjectRepository;
import com.ibit.chatbot.service.OpenAIFileService;

@RestController
@RequestMapping("/projects")
public class FileController {

    private final ProjectRepository projectRepository;
    private final ProjectFileRepository projectFileRepository;
    private final OpenAIFileService openAIFileService;

    public FileController(ProjectRepository projectRepository,
                          ProjectFileRepository projectFileRepository,
                          OpenAIFileService openAIFileService) {
        this.projectRepository = projectRepository;
        this.projectFileRepository = projectFileRepository;
        this.openAIFileService = openAIFileService;
    }

    
    @PostMapping(
    	    value = "/{projectId}/files",
    	    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    	)
    
    public ProjectFile uploadFile(
            @PathVariable Long projectId,
            @RequestPart("file") MultipartFile file,
            @AuthenticationPrincipal User user) throws IOException {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!project.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        String openAiFileId = openAIFileService.uploadFile(file);
        

        ProjectFile projectFile = new ProjectFile();
        projectFile.setFileName(file.getOriginalFilename());
        projectFile.setOpenAiFileId(openAiFileId);
        projectFile.setProject(project);

        return projectFileRepository.save(projectFile);
    }
}

