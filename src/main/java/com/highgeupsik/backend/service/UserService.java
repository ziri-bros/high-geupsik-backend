package com.highgeupsik.backend.service;

import static com.highgeupsik.backend.utils.ErrorMessage.SCHOOL_NOT_FOUND;
import static com.highgeupsik.backend.utils.ErrorMessage.USER_NOT_FOUND;

import com.highgeupsik.backend.dto.SchoolDTO;
import com.highgeupsik.backend.dto.StudentCardDTO;
import com.highgeupsik.backend.dto.TokenDTO;
import com.highgeupsik.backend.entity.GRADE;
import com.highgeupsik.backend.entity.StudentCard;
import com.highgeupsik.backend.entity.User;
import com.highgeupsik.backend.entity.UserConfirm;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.jwt.JwtTokenProvider;
import com.highgeupsik.backend.repository.SchoolRepository;
import com.highgeupsik.backend.repository.StudentCardRepository;
import com.highgeupsik.backend.repository.UserConfirmRepository;
import com.highgeupsik.backend.repository.UserRepository;
import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserConfirmRepository userConfirmRepository;
    private final SchoolRepository schoolRepository;
    private final StudentCardRepository studentCardRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final MailService mailService;

    public void updateUserInfo(Long userId, StudentCardDTO studentCardDTO, SchoolDTO schoolDTO) {
        User user = userRepository.findById(userId).orElseThrow(
            () -> new NotFoundException(USER_NOT_FOUND));
        user.updateRoleGuest();
        user.setSchool(schoolRepository.findByName(schoolDTO.getName()).orElseThrow(
            () -> new NotFoundException(SCHOOL_NOT_FOUND)));
        user.setStudentCard(studentCardRepository.save(new StudentCard(
            GRADE.from(studentCardDTO.getGrade()), studentCardDTO.getClassNum(),
            studentCardDTO.getStudentCardImage())));
        saveUserConfirm(user);
    }

    public Long saveUserConfirm(User user) {
        return userConfirmRepository.save(UserConfirm.builder()
            .user(user)
            .studentCard(user.getStudentCard())
            .build()).getId();
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

    public void acceptUser(Long userId) throws MessagingException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        user.updateRoleUser();
        userConfirmRepository.deleteByUserId(userId);
        mailService.sendEmail(user.getUsername(), user.getEmail(), true);
    }

    public void rejectUser(Long userId) throws MessagingException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        userConfirmRepository.deleteByUserId(userId);
        mailService.sendEmail(user.getUsername(), user.getEmail(), false);
    }
}
