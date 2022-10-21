package com.highgeupsik.backend.repository.user;

import com.highgeupsik.backend.entity.user.AuthProvider;
import com.highgeupsik.backend.entity.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u,s,sc from User u "
        + "left join StudentCard s "
        + "on u.studentCard = s "
        + "left join School sc "
        + "on s.school = sc "
        + "where u.id = :userId")
    Optional<User> findById(@Param("userId") Long userId);

    Optional<User> findByEmailAndProvider(String email, AuthProvider provider);

    Optional<User> findByRefreshToken(String refreshToken);
}
