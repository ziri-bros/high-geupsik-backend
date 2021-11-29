package com.highgeupsik.backend.service;


import static com.highgeupsik.backend.utils.ErrorMessage.SCHOOL_NOT_FOUND;
import static com.highgeupsik.backend.utils.ErrorMessage.TOKEN_EXPIRED;
import static com.highgeupsik.backend.utils.ErrorMessage.USER_NOT_FOUND;

import com.highgeupsik.backend.dto.SchoolDTO;
import com.highgeupsik.backend.dto.TokenDTO;
import com.highgeupsik.backend.entity.StudentCard;
import com.highgeupsik.backend.entity.User;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.exception.TokenExpiredException;
import com.highgeupsik.backend.jwt.JwtTokenProvider;
import com.highgeupsik.backend.repository.SchoolRepository;
import com.highgeupsik.backend.repository.StudentCardRepository;
import com.highgeupsik.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;
    private final StudentCardRepository studentCardRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public void updateUserInfo(Long userId, String studentCardImage, SchoolDTO schoolDTO) {
        User user = userRepository.findById(userId).orElseThrow(
            () -> new NotFoundException(USER_NOT_FOUND));
        user.updateSchool(schoolRepository.findByName(schoolDTO.getName()).orElseThrow(
            () -> new NotFoundException(SCHOOL_NOT_FOUND)));
        user.updateStudentCard(studentCardRepository.save(new StudentCard(studentCardImage)));
    }

    public void updateSchool(Long userId, SchoolDTO schoolDTO){
        User user = userRepository.findById(userId).orElseThrow(
            () -> new NotFoundException(USER_NOT_FOUND));
        user.updateSchool(schoolRepository.findByName(schoolDTO.getName()).orElseThrow(
            () -> new NotFoundException(SCHOOL_NOT_FOUND)));
    }

    public void updateRole(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
            new NotFoundException(USER_NOT_FOUND));
        user.updateRole();
    }

    public TokenDTO updateToken(TokenDTO tokenDTO) {
        String refreshToken = tokenDTO.getRefreshToken();
        String accessToken = tokenDTO.getAccessToken();
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new TokenExpiredException(TOKEN_EXPIRED);
        }
        Long userId = jwtTokenProvider.getUserPK(accessToken);
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        String role = user.getRole().toString();
        TokenDTO newTokenDTO = new TokenDTO(jwtTokenProvider.createNewToken(userId, role, "access"),
            jwtTokenProvider.createRefreshToken());
        user.updateToken(jwtTokenProvider.createNewToken(userId, role, "refresh"));
        return newTokenDTO;
    }

    public TokenDTO login(Long userId) {
        User user = userRepository.findById(userId).get();
        String role = user.getRole().toString();
        String accessToken = jwtTokenProvider.createNewToken(userId, role, "access");
        String refreshToken = jwtTokenProvider.createRefreshToken();
        user.updateToken(jwtTokenProvider.createNewToken(userId, role, "refresh"));
        return new TokenDTO(accessToken, refreshToken);
    }

}
