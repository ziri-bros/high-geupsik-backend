package com.highgeupsik.backend.service;

import static com.highgeupsik.backend.utils.ErrorMessage.*;

import com.highgeupsik.backend.dto.SchoolReqDTO;
import com.highgeupsik.backend.dto.StudentCardDTO;
import com.highgeupsik.backend.entity.GRADE;
import com.highgeupsik.backend.entity.StudentCard;
import com.highgeupsik.backend.entity.User;
import com.highgeupsik.backend.entity.UserConfirm;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.repository.SchoolRepository;
import com.highgeupsik.backend.repository.StudentCardRepository;
import com.highgeupsik.backend.repository.UserConfirmRepository;
import com.highgeupsik.backend.repository.UserRepository;
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
    private final MailService mailService;

    public void modifyUser(Long userId, StudentCardDTO studentCardDTO, SchoolReqDTO schoolReqDTO) {
        User user = userRepository.findById(userId).orElseThrow(
            () -> new NotFoundException(USER_NOT_FOUND));
        user.updateRoleGuest();
        user.setSchool(schoolRepository.findByRegionAndName(schoolReqDTO.getRegion(), schoolReqDTO.getSchoolName())
            .orElseThrow(() -> new NotFoundException(SCHOOL_NOT_FOUND)));
        user.setStudentCard(studentCardRepository.save(new StudentCard(
            GRADE.from(studentCardDTO.getGrade()), studentCardDTO.getClassNum(),
            studentCardDTO.getStudentCardImage())));
        saveUserConfirm(user);
    }

    public void saveUserConfirm(User user) {
        userConfirmRepository.save(UserConfirm.builder()
            .user(user)
            .studentCard(user.getStudentCard())
            .build());
    }

    public void acceptUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        user.updateRoleUser();
        userConfirmRepository.deleteByUserId(userId);
        mailService.sendEmail(user.getUsername(), user.getEmail(), true);
    }

    public void rejectUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        userConfirmRepository.deleteByUserId(userId);
        mailService.sendEmail(user.getUsername(), user.getEmail(), false);
    }
}
