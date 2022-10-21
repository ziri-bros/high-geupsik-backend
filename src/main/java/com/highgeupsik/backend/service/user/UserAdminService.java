package com.highgeupsik.backend.service.user;

import static com.highgeupsik.backend.exception.ErrorMessages.USER_NOT_FOUND;

import com.highgeupsik.backend.entity.user.User;
import com.highgeupsik.backend.exception.ResourceNotFoundException;
import com.highgeupsik.backend.repository.user.UserConfirmRepository;
import com.highgeupsik.backend.repository.user.UserRepository;
import com.highgeupsik.backend.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserAdminService {

    private final UserRepository userRepository;
    private final UserConfirmRepository userConfirmRepository;
    private final MailService mailService;

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
