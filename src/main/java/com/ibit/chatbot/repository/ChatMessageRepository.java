package com.ibit.chatbot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ibit.chatbot.entity.ChatMessage;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

List<ChatMessage> findByProjectIdOrderByCreatedAtAsc(Long projectId);
}
