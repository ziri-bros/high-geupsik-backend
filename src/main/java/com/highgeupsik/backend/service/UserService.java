package com.highgeupsik.backend.service;

import static com.highgeupsik.backend.utils.ErrorMessage.SCHOOL_NOT_FOUND;
import static com.highgeupsik.backend.utils.ErrorMessage.TOKEN_EXPIRED;
import static com.highgeupsik.backend.utils.ErrorMessage.USER_NOT_FOUND;

import com.highgeupsik.backend.dto.SchoolDTO;
import com.highgeupsik.backend.dto.StudentCardDTO;
import com.highgeupsik.backend.dto.TokenDTO;
import com.highgeupsik.backend.entity.GRADE;
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

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;
    private final StudentCardRepository studentCardRepository;
    private final UserConfirmService userConfirmService;
    private final JwtTokenProvider jwtTokenProvider;

    public void updateUserInfo(Long userId, StudentCardDTO studentCardDTO, SchoolDTO schoolDTO) {
        User user = userRepository.findById(userId).orElseThrow(
            () -> new NotFoundException(USER_NOT_FOUND));
        user.updateRoleGuest();
        user.setSchool(schoolRepository.findByName(schoolDTO.getName()).orElseThrow(
            () -> new NotFoundException(SCHOOL_NOT_FOUND)));
        user.setStudentCard(studentCardRepository.save(new StudentCard(
            GRADE.from(studentCardDTO.getGrade()), studentCardDTO.getClassNum(),
            studentCardDTO.getStudentCardImage())));
        userConfirmService.saveUserConfirm(user);
    }

    public void updateRoleUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
            new NotFoundException(USER_NOT_FOUND));
        user.updateRoleUser();
    }

    public TokenDTO updateToken(TokenDTO tokenDTO) {
        String refreshToken = tokenDTO.getRefreshToken();
        String accessToken = tokenDTO.getAccessToken();
        jwtTokenProvider.validateToken(refreshToken);
        Long userId = jwtTokenProvider.getUserPK(accessToken);
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        String role = user.getRole().toString();
        TokenDTO newTokenDTO = new TokenDTO(jwtTokenProvider.createNewToken(userId, role, "access"),
            jwtTokenProvider.createRefreshToken());
        user.updateToken(jwtTokenProvider.createNewToken(userId, role, "refresh"));
        return newTokenDTO;
    }
}
