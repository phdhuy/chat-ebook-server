package com.chatebook.file.repository;

import com.chatebook.file.model.File;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, UUID> {

  @Query(value = "SELECT f FROM files f")
  Page<File> getListFileByAdmin(Pageable pageable);
}
