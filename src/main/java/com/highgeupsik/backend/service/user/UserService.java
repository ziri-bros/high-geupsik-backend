package com.highgeupsik.backend.service.user;

import static com.highgeupsik.backend.exception.ErrorMessages.*;

import com.highgeupsik.backend.entity.school.GRADE;
import com.highgeupsik.backend.entity.school.Region;
import com.highgeupsik.backend.entity.school.StudentCard;
import com.highgeupsik.backend.entity.user.User;
import com.highgeupsik.backend.entity.user.UserConfirm;
import com.highgeupsik.backend.exception.ResourceNotFoundException;
import com.highgeupsik.backend.repository.school.SchoolRepository;
import com.highgeupsik.backend.repository.user.StudentCardRepository;
import com.highgeupsik.backend.repository.user.UserConfirmRepository;
import com.highgeupsik.backend.repository.user.UserRepository;
import com.highgeupsik.backend.service.MailService;
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

    public void updateUser(Long userId, int grade, int classNum, String studentCardImage, Region region, String schoolName) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        user.updateRoleGuest();
        user.setSchool(schoolRepository.findByRegionAndName(region, schoolName)
            .orElseThrow(() -> new ResourceNotFoundException(SCHOOL_NOT_FOUND)));
        user.setStudentCard(studentCardRepository
            .save(new StudentCard(GRADE.from(grade), classNum, studentCardImage)));
        saveUserConfirm(user);
    }

    public void saveUserConfirm(User user) {
        userConfirmRepository.save(UserConfirm.builder()
            .user(user)
            .studentCard(user.getStudentCard())
            .build());
    }

    public void acceptUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        user.updateRoleUser();
        userConfirmRepository.deleteByUserId(userId);
        mailService.sendEmail(user.getUsername(), user.getEmail(), true);
    }

    public void rejectUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        userConfirmRepository.deleteByUserId(userId);
        mailService.sendEmail(user.getUsername(), user.getEmail(), false);
    }
}
