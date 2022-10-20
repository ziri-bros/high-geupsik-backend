package com.highgeupsik.backend.oauth2;

import com.highgeupsik.backend.entity.user.User;
import com.highgeupsik.backend.jwt.JwtTokenProvider;
import com.highgeupsik.backend.oauth2.SocialUser;
import com.highgeupsik.backend.repository.user.UserConfirmRepository;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserConfirmRepository userConfirmRepository;
    @Value("${redirect_url}")
    String targetUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {
        SocialUser socialUser = (SocialUser) authentication.getPrincipal();
        User user = socialUser.getUser();
        Long userId = user.getId();
        return UriComponentsBuilder.fromUriString(targetUrl)
            .queryParam("role", user.getRole())
            .queryParam("isSubmittedCard", isSubmittedUserConfirm(userId))
            .queryParam("accessToken", jwtTokenProvider.createAccessToken(userId, user.getRole().toString()))
            .queryParam("refreshToken", user.getRefreshToken())
            .build()
            .toUriString();
    }

    public boolean isSubmittedUserConfirm(Long userId) {
        return userConfirmRepository.findByUserId(userId).isPresent();
    }
}
