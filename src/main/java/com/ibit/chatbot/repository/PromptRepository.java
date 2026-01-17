package com.ibit.chatbot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ibit.chatbot.entity.Prompt;

@Repository
public interface PromptRepository extends JpaRepository<Prompt, Long> {

    Optional<Prompt> findByProjectId(Long projectId);
}
