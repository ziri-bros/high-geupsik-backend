package com.highgeupsik.backend.service.user;

import static com.highgeupsik.backend.exception.ErrorMessages.USER_NOT_FOUND;

import com.highgeupsik.backend.api.user.UserTokenDTO;
import com.highgeupsik.backend.entity.user.User;
import com.highgeupsik.backend.exception.ResourceNotFoundException;
import com.highgeupsik.backend.jwt.JwtTokenProvider;
import com.highgeupsik.backend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserTokenService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public UserTokenDTO getTokenDTO(UserTokenDTO userTokenDTO) {
        String refreshToken = userTokenDTO.getRefreshToken();
        jwtTokenProvider.validateToken(refreshToken);
        String newRefreshToken = jwtTokenProvider.createRefreshToken();
        User user = userRepository.findByRefreshToken(refreshToken)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        user.updateRefreshToken(newRefreshToken);
        return new UserTokenDTO(jwtTokenProvider.createAccessToken(user.getId(), user.getRole().toString()),
            newRefreshToken);
    }
}
