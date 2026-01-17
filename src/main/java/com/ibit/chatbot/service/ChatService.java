package com.ibit.chatbot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ibit.chatbot.dto.ChatResponse;
import com.ibit.chatbot.entity.ChatMessage;
import com.ibit.chatbot.entity.Project;
import com.ibit.chatbot.entity.Prompt;
import com.ibit.chatbot.entity.User;
import com.ibit.chatbot.repository.ChatMessageRepository;
import com.ibit.chatbot.repository.ProjectRepository;
import com.ibit.chatbot.repository.PromptRepository;

@Service
public class ChatService {

	private static final int MEMORY_LIMIT = 10;

	private final ProjectRepository projectRepository;
	private final ChatMessageRepository chatMessageRepository;

	private final OpenRouterService openRouterService;
	private final PromptRepository promptRepository;

	public ChatService(ProjectRepository projectRepository, ChatMessageRepository chatMessageRepository,
			OpenRouterService openRouterService, PromptRepository promptRepository) {

		this.projectRepository = projectRepository;
		this.chatMessageRepository = chatMessageRepository;
		this.openRouterService = openRouterService;
		this.promptRepository = promptRepository;
	}

	public ChatResponse chat(Long projectId, String message, User user) {

		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new RuntimeException("Project not found"));

		// ownership check
		if (!project.getUser().getId().equals(user.getId())) {
			throw new RuntimeException("Unauthorized");
		}

		// save USER message
		ChatMessage userMsg = new ChatMessage();
		userMsg.setRole("USER");
		userMsg.setContent(message);
		userMsg.setProject(project);

		chatMessageRepository.save(userMsg);



		Prompt prompt = promptRepository.findByProjectId(projectId)
				.orElseThrow(() -> new RuntimeException("Prompt not found"));

		

		List<ChatMessage> history = chatMessageRepository.findByProjectIdOrderByCreatedAtAsc(projectId);

		// limit memory window

		if (history.size() > MEMORY_LIMIT) {
			history = history.subList(history.size() - MEMORY_LIMIT, history.size());
		}

		List<Map<String, String>> messages = new ArrayList<>();

		// system prompt(brain)
		messages.add(Map.of("role", "system", "content", prompt.getContent()));

		// add history message
		for (ChatMessage msg : history) {
			messages.add(Map.of("role", msg.getRole().toLowerCase(), "content", msg.getContent()));
		}

		// add new user message
		messages.add(Map.of("role", "user", "content", message));

		// call AI
		String assistantReply = openRouterService.getAIResponse(messages);

		ChatMessage assistantMsg = new ChatMessage();
		assistantMsg.setRole("ASSISTANT");
		assistantMsg.setContent(assistantReply);
		assistantMsg.setProject(project);

		chatMessageRepository.save(assistantMsg);

		return new ChatResponse(assistantReply);
	}
}
