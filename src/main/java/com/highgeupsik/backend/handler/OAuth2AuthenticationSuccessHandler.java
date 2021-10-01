package com.highgeupsik.backend.handler;

import com.highgeupsik.backend.dto.TokenDTO;
import com.highgeupsik.backend.entity.User;
import com.highgeupsik.backend.jwt.JwtTokenProvider;
import com.highgeupsik.backend.oauth2.SocialUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.highgeupsik.backend.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info(authentication.getAuthorities().toString());
        SocialUser socialUser = (SocialUser) authentication.getPrincipal();
        User user = socialUser.getUser();
        String accessToken = jwtTokenProvider.createNewToken(user.getId(), user.getRole().toString(), "access");
        String refreshToken = jwtTokenProvider.createRefreshToken();
        response.getWriter().write(this.objectMapper.writeValueAsString((ApiUtils.success(new TokenDTO(accessToken, refreshToken)))));
    }
}
