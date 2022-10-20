package com.highgeupsik.backend.oauth2;

import com.highgeupsik.backend.entity.user.User;
import com.highgeupsik.backend.jwt.JwtTokenProvider;
import com.highgeupsik.backend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {

        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(oAuth2UserRequest);

        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId(); //소셜 서비스 구분
        String userNameAttributeName = oAuth2UserRequest.getClientRegistration().getProviderDetails()
            .getUserInfoEndpoint().getUserNameAttributeName(); //소셜의 키값
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName,
            oAuth2User.getAttributes());
        User user = saveOrUpdate(attributes);
        return SocialUser.of(user);
//        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(role))
//                , attributes.getAttributes(), attributes.getNameAttributeKey());
    }

    public User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmailAndProvider(attributes.getEmail(), attributes.getProvider())
            .map(entity -> entity.updateName(attributes.getName()))
            .orElse(attributes.toEntity());
        user.updateRefreshToken(jwtTokenProvider.createRefreshToken());
        return userRepository.save(user);
    }
}
