package com.highgeupsik.backend.repository.user;

import com.highgeupsik.backend.entity.user.AuthProvider;
import com.highgeupsik.backend.entity.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"studentCard", "school"})
    Optional<User> findById(Long userId);

    Optional<User> findByEmailAndProvider(String email, AuthProvider provider);

    Optional<User> findByRefreshToken(String refreshToken);
}
