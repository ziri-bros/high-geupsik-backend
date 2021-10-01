package com.highgeupsik.backend.service;


import com.highgeupsik.backend.dto.SchoolInfoDTO;
import com.highgeupsik.backend.dto.TokenDTO;
import com.highgeupsik.backend.entity.ConfirmToken;
import com.highgeupsik.backend.entity.Role;
import com.highgeupsik.backend.entity.SchoolInfo;
import com.highgeupsik.backend.entity.User;
import com.highgeupsik.backend.exception.DuplicateException;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.exception.TokenExpiredException;
import com.highgeupsik.backend.jwt.JwtTokenProvider;
import com.highgeupsik.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.highgeupsik.backend.utils.ErrorMessage.*;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private static final String fileName = "기본이미지";

    private static final String fileUrl = "https://hstime-bucket.s3.ap-northeast-2.amazonaws.com/" +
            "%EA%B8%B0%EB%B3%B8%EC%9D%B4%EB%AF%B8%EC%A7%80.PNG";

    public Long saveUser(String email, String password, String nickname,
                         String username, SchoolInfo schoolInfo) {
        return userRepository.save(User.builder()
                .email(email)
                .username(username)
                .role(Role.ROLE_USER) //테스트
                .schoolInfo(schoolInfo)
                .build()).getId();
    }

    public void verifyEmailToken(ConfirmToken confirmToken) {
        User user = userRepository.findById(confirmToken.getUserId()).get();
        user.updateRole();
        confirmToken.useToken();
    }

    public void checkDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email))
            throw new DuplicateException(RESOURCE_EXIST);
    }

    public void updateSchoolInfo(Long userId, SchoolInfoDTO schoolInfoDTO) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(USER_NOT_FOUND));
        user.updateSchoolInfo(new SchoolInfo(schoolInfoDTO.getSchoolName(),
                schoolInfoDTO.getSchoolCode(), schoolInfoDTO.getRegion()));
    }

    public void updateRole(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(USER_NOT_FOUND));
        user.updateRole();
    }

    public TokenDTO updateToken(TokenDTO tokenDTO) {
        String refreshToken = tokenDTO.getRefreshToken();
        String accessToken = tokenDTO.getAccessToken();
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new TokenExpiredException(TOKEN_EXPIRED);
        }
        Long userId = jwtTokenProvider.getUserPK(accessToken);
        User user = userRepository.findById(userId).get();
        String role = user.getRole().toString();
        TokenDTO newTokenDTO = new TokenDTO(jwtTokenProvider.createNewToken(userId, role, "access"),
                jwtTokenProvider.createRefreshToken());
        user.updateToken(jwtTokenProvider.createNewToken(userId, role, "refresh"));
        return newTokenDTO;
    }

    public TokenDTO login(Long userId) {
        User user = userRepository.findById(userId).get();
        String role = user.getRole().toString();
        String accessToken = jwtTokenProvider.createNewToken(userId, role, "access");
        String refreshToken = jwtTokenProvider.createRefreshToken();
        user.updateToken(jwtTokenProvider.createNewToken(userId, role, "refresh"));
        return new TokenDTO(accessToken, refreshToken);
    }

}
