package com.highgeupsik.backend.service.user;

import static com.highgeupsik.backend.exception.ErrorMessages.*;

import com.highgeupsik.backend.entity.school.GRADE;
import com.highgeupsik.backend.entity.school.Region;
import com.highgeupsik.backend.entity.school.School;
import com.highgeupsik.backend.entity.school.StudentCard;
import com.highgeupsik.backend.entity.user.User;
import com.highgeupsik.backend.entity.user.UserConfirm;
import com.highgeupsik.backend.exception.ResourceNotFoundException;
import com.highgeupsik.backend.repository.school.SchoolRepository;
import com.highgeupsik.backend.repository.user.StudentCardRepository;
import com.highgeupsik.backend.repository.user.UserConfirmRepository;
import com.highgeupsik.backend.repository.user.UserRepository;
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

    public void updateUser(Long userId, int grade, int classNum, String studentCardImage, Region region, String schoolName) {
        School school = schoolRepository.findByRegionAndName(region, schoolName)
            .orElseThrow(() -> new ResourceNotFoundException(SCHOOL_NOT_FOUND));
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        StudentCard studentCard = studentCardRepository
            .save(new StudentCard(school, GRADE.from(grade), classNum, studentCardImage));
        user.updateRoleGuest();
        user.setStudentCard(studentCard);
        saveUserConfirm(user);
    }

    public void saveUserConfirm(User user) {
        userConfirmRepository.save(UserConfirm.builder()
            .user(user)
            .studentCard(user.getStudentCard())
            .build());
    }
}
