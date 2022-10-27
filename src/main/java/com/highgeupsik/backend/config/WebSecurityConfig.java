package com.highgeupsik.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highgeupsik.backend.jwt.JwtAccessDeniedHandler;
import com.highgeupsik.backend.jwt.JwtAuthenticationEntryPoint;
import com.highgeupsik.backend.jwt.JwtAuthenticationFilter;
import com.highgeupsik.backend.jwt.JwtExceptionFilter;
import com.highgeupsik.backend.jwt.JwtTokenProvider;
import com.highgeupsik.backend.oauth2.CustomOAuth2UserService;
import com.highgeupsik.backend.oauth2.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableWebSecurity //스프링 시큐리티 필터체인 사용
@Configuration
public class WebSecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final ObjectMapper objectMapper;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/favicon.ico", "/v2/**", "/webjars/**", "/swagger**",
            "/swagger-resources/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .httpBasic().disable()
            .csrf().disable()
            .formLogin().disable()
            .cors()

            .and()
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(jwtAccessDeniedHandler)

            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션허용안함

            .and()
            .authorizeRequests()
            .antMatchers("/login").permitAll()
            .antMatchers("/login/**").permitAll()
            .antMatchers("/images").permitAll()
            .antMatchers("/users").permitAll()
            .antMatchers("/schools").permitAll()
            .antMatchers("/admin/**").hasRole("ADMIN")
            .antMatchers("/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
            .anyRequest()
            .authenticated()

            .and()
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new JwtExceptionFilter(objectMapper), JwtAuthenticationFilter.class)
            .oauth2Login()
            .authorizationEndpoint()
            .baseUri("/oauth2/authorize")

            .and()
            .userInfoEndpoint()
            .userService(customOAuth2UserService)

            .and()
            .successHandler(oAuth2AuthenticationSuccessHandler)
            .and().build();
    }
}