package com.highgeupsik.backend.service;


import com.highgeupsik.backend.dto.FollowDTO;
import com.highgeupsik.backend.entity.Follow;
import com.highgeupsik.backend.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.highgeupsik.backend.utils.PagingUtils.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowQueryService {

    private final FollowRepository followRepository;
    private static final int FRIEND_COUNT = 5;

    //보낸 친구요청 조회
    public List<FollowDTO> findSentFollowRequests(Long userId, Integer pageNum) {
        List<Follow> follows = followRepository.findByFromUserIdAndAcceptFlag(userId, false,
                orderByCreatedDateDESC(pageNum, FRIEND_COUNT)).getContent();
        return follows.stream().map((follow) -> new FollowDTO(follow)).collect(Collectors.toList());
    }

    //받은 친구요청 조회
    public List<FollowDTO> findReceivedFollowRequests(Long userId, Integer pageNum) {
        List<Follow> follows = followRepository.findByToUserIdAndAcceptFlag(userId, false,
                orderByCreatedDateDESC(pageNum, FRIEND_COUNT)).getContent();
        return follows.stream().map((follow) -> new FollowDTO(follow)).collect(Collectors.toList());
    }

    //친구목록조회
    public List<FollowDTO> findFollows(Long userId, Integer pageNum) {
        List<Follow> follows = followRepository.findByToUserIdAndAcceptFlag(userId, true,
                orderByUserName(pageNum, FRIEND_COUNT)).getContent();
        return follows.stream().map((follow) -> new FollowDTO(follow)).collect(Collectors.toList());
    }
}
