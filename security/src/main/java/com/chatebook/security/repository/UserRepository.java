package com.chatebook.security.repository;

import com.chatebook.security.model.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  boolean existsByEmail(String email);

  Optional<User> findByEmail(String email);

  @Query(
      value =
          "SELECT u FROM users u")
  Page<User> getListUser(Pageable pageable);
}
