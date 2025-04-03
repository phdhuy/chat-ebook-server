package com.chatebook.file.repository;

import java.util.UUID;

import com.chatebook.file.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, UUID> {}
