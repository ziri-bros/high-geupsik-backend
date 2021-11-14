package com.highgeupsik.backend.service;


import static com.highgeupsik.backend.utils.ErrorMessage.USER_NOT_FOUND;

import com.highgeupsik.backend.dto.UploadFileDTO;
import com.highgeupsik.backend.dto.UserCardReqDTO;
import com.highgeupsik.backend.entity.UploadFile;
import com.highgeupsik.backend.entity.UserCard;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.repository.UserCardRepository;
import com.highgeupsik.backend.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCardService {

    private final UserCardRepository userCardRepository;
    private final UserRepository userRepository;

    public Long saveUserCard(Long userId, UploadFileDTO uploadFileDTO) {
        return userCardRepository.save(UserCard.builder()
            .user(userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(USER_NOT_FOUND)))
            .uploadFile(new UploadFile(uploadFileDTO.getFileName(), uploadFileDTO.getFileDownloadUri()))
            .build()).getId();
    }

    public void deleteUserCard(Long cardId) {
        userCardRepository.deleteById(cardId);
    }

    public void deleteUserCardByUserId(Long userId) {
        userCardRepository.deleteByUserId(userId);
    }
}
