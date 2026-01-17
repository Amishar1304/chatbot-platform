package com.ibit.chatbot.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.ibit.chatbot.dto.ChatRequest;
import com.ibit.chatbot.dto.ChatResponse;
import com.ibit.chatbot.entity.User;
import com.ibit.chatbot.service.ChatService;

@RestController
@RequestMapping("/projects")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/{projectId}/chat")
    public ChatResponse chat(
            @PathVariable Long projectId,
            @RequestBody ChatRequest request,
            @AuthenticationPrincipal User user) {

        return chatService.chat(projectId, request.getMessage(), user);
    }
}
