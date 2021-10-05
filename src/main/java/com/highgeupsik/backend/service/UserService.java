package com.highgeupsik.backend.service;


import com.highgeupsik.backend.dto.SchoolInfoDTO;
import com.highgeupsik.backend.dto.TokenDTO;
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

    public Long saveUser(String email, String password, String nickname,
                         String username, SchoolInfo schoolInfo) {
        return userRepository.save(User.builder()
                .email(email)
                .username(username)
                .role(Role.ROLE_USER) //테스트
                .schoolInfo(schoolInfo)
                .build()).getId();
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
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
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
