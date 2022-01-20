package com.highgeupsik.backend.handler;

import com.highgeupsik.backend.entity.User;
import com.highgeupsik.backend.jwt.JwtTokenProvider;
import com.highgeupsik.backend.oauth2.SocialUser;
import com.highgeupsik.backend.repository.UserConfirmRepository;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserConfirmRepository userConfirmRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {
        log.info(authentication.getAuthorities().toString());
        String targetUrl = determineTargetUrl(request, response, authentication);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {
        String targetUrl = "http://localhost:3000/oauth2/redirect?";
        SocialUser socialUser = (SocialUser) authentication.getPrincipal();
        User user = socialUser.getUser();
        String accessToken = jwtTokenProvider.createNewToken(user.getId(), user.getRole().toString(), "access");
        String refreshToken = jwtTokenProvider.createRefreshToken();
        boolean isSubmittedCard = checkUserConfirm(user.getId());
        return UriComponentsBuilder.fromUriString(targetUrl)
            .queryParam("role", user.getRole())
            .queryParam("isSubmittedCard", isSubmittedCard)
            .queryParam("accessToken", accessToken)
            .queryParam("refreshToken", refreshToken)
            .build()
            .toUriString();
    }

    public boolean checkUserConfirm(Long userId) {
        return userConfirmRepository.findByUserId(userId).isPresent();
    }
}
