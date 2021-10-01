package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.entity.AuthProvider;
import com.highgeupsik.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //    findAll(Pageable pageable)
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndProvider(String email, AuthProvider provider);

    boolean existsByEmail(String email);

}
