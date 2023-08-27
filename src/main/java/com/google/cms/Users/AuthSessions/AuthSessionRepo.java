package com.google.cms.Users.AuthSessions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthSessionRepo extends JpaRepository<AuthSession, Long> {
    Optional<AuthSession> findByRefreshToken(String refreshToken);

    List<AuthSession> findByLoginTime(LocalDateTime loginTime);

    @Query(value = "SELECT * FROM auth_session where DATE(login_time) =:date", nativeQuery = true)
    List<AuthSession> findByDate(String date);
}


