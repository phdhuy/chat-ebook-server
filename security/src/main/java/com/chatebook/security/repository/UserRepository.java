package com.chatebook.security.repository;

import com.chatebook.security.model.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  boolean existsByEmail(String email);

  Optional<User> findByEmail(String email);

  @Query(
      value =
          "SELECT * FROM users WHERE username ILIKE CONCAT('%', :query, '%') OR email ILIKE CONCAT('%', :query, '%')",
      nativeQuery = true)
  Page<User> getListUser(Pageable pageable, @Param("query") String query);
}
