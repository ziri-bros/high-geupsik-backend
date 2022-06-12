package com.highgeupsik.backend.service;

import static com.highgeupsik.backend.utils.PagingUtils.*;

import com.highgeupsik.backend.dto.NotificationDTO;
import com.highgeupsik.backend.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NotificationQueryService {

    private final NotificationRepository notificationRepository;
    private static final int NOTIFICATION_COUNT = 10;

    public Page<NotificationDTO> findAllByUserId(Long userId, Integer pageNum) {
        return notificationRepository.findAllByReceiverId(userId, orderByCreatedDateDESC(pageNum, NOTIFICATION_COUNT))
            .map(NotificationDTO::new);
    }
}
