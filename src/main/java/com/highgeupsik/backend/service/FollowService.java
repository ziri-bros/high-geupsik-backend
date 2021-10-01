package com.highgeupsik.backend.service;


import com.highgeupsik.backend.entity.Follow;
import com.highgeupsik.backend.exception.DuplicateException;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.repository.FollowRepository;
import com.highgeupsik.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    //친구요청
    public Long saveFollow(Long fromUserId, Long toUserId,
                           String fromUserName, boolean acceptFlag) {
        return followRepository.save(Follow.builder()
                .fromUser(userRepository.findById(fromUserId).get())
                .toUser(userRepository.findById(toUserId).get())
                .fromUserName(fromUserName)
                .acceptFlag(acceptFlag)
                .build()).getId();
    }

    //친구요청 중복체크
    public void checkDuplicateFollow(Long fromUserId, Long toUserId) {
        Optional<Follow> follow1 = followRepository.findByFromUserIdAndToUserId(fromUserId, toUserId);
        Optional<Follow> follow2 = followRepository.findByFromUserIdAndToUserId(toUserId, fromUserId);
        if (follow1.isPresent()) {
            throw new DuplicateException("이미 보낸 요청입니다");
        }
        if (follow2.isPresent()) {
            throw new DuplicateException("이미 상대방에게 요청이 왔습니다");
        }
    }

    //친구요청 수락
    public void acceptFollow(Long fromUserId, Long toUserId, String fromUserName) {
        saveFollow(fromUserId, toUserId, fromUserName, true);//반대 테이블 생성
        Follow follow = followRepository.findByFromUserIdAndToUserId(fromUserId, toUserId).get();
        follow.acceptFollow();
    }

    //친구삭제
    public void deleteFriend(Long followId) {
        Follow follow = followRepository.findById(followId).get();
        followRepository.delete(follow);
    }

    //친구요청 거부
    public void deleteFollowRequest(Long userId, Long fromUserId) {
        Follow follow = followRepository.findByFromUserIdAndToUserId(fromUserId, userId)
                .orElseThrow(() -> new NotFoundException("없는 친구요청 입니다"));
        followRepository.delete(follow);
    }
}
