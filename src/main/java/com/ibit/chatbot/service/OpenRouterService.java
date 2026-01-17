package com.ibit.chatbot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenRouterService {

	@Value("${openrouter.api.url}")
	private String apiUrl;

	@Value("${openrouter.api.key}")
	private String apiKey;

	@Value("${openrouter.api.model}")
	private String model;

	private final RestTemplate restTemplate = new RestTemplate();




	public String getAIResponse(List<Map<String, String>> messages) {

		Map<String, Object> body = new HashMap<>();
		body.put("model", model);
		body.put("messages", messages);

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(apiKey);
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("HTTP-Referer", "http://localhost");
		headers.add("X-Title", "Chatbot Platform");

		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

		ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);

		List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");

		Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

		return message.get("content").toString();
	}

}
