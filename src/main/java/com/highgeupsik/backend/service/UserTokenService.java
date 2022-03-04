package com.highgeupsik.backend.service;

import static com.highgeupsik.backend.utils.ErrorMessage.USER_NOT_FOUND;

import com.highgeupsik.backend.dto.TokenDTO;
import com.highgeupsik.backend.entity.User;
import com.highgeupsik.backend.exception.ResourceNotFoundException;
import com.highgeupsik.backend.jwt.JwtTokenProvider;
import com.highgeupsik.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserTokenService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenDTO getTokenDTO(TokenDTO tokenDTO) {
        String refreshToken = tokenDTO.getRefreshToken();
        jwtTokenProvider.validateToken(refreshToken);
        String newRefreshToken = jwtTokenProvider.createRefreshToken();
        User user = userRepository.findByRefreshToken(refreshToken)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        user.updateRefreshToken(newRefreshToken);
        return new TokenDTO(jwtTokenProvider.createAccessToken(user.getId(), user.getRole().toString()),
            newRefreshToken);
    }
}
