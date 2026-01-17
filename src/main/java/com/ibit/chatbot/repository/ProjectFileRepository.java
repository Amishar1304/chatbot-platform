package com.ibit.chatbot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ibit.chatbot.entity.ProjectFile;

@Repository
public interface ProjectFileRepository extends JpaRepository<ProjectFile, Long> {

	List<ProjectFile> findByProjectId(Long projectId);
}
