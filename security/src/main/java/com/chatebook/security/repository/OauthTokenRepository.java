package com.chatebook.security.repository;

import com.chatebook.security.model.OauthToken;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OauthTokenRepository extends JpaRepository<OauthToken, UUID> {

  Optional<OauthToken> findByRefreshToken(UUID refreshToken);
}
