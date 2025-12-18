package com.aivle._th_miniProject.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
    Optional<User> findByRefreshToken(String refreshToken);

    boolean existsByName(String name);
    boolean existsByEmail(String email);


}
