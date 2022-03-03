package com.highgeupsik.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highgeupsik.backend.handler.JwtAccessDeniedHandler;
import com.highgeupsik.backend.handler.JwtAuthenticationEntryPoint;
import com.highgeupsik.backend.handler.OAuth2AuthenticationSuccessHandler;
import com.highgeupsik.backend.jwt.JwtAuthenticationFilter;
import com.highgeupsik.backend.jwt.JwtExceptionFilter;
import com.highgeupsik.backend.jwt.JwtTokenProvider;
import com.highgeupsik.backend.oauth2.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableWebSecurity //스프링 시큐리티 필터체인 사용
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

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

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().mvcMatchers("/favicon.ico",
            "/v2/**",
            "/webjars/**",
            "/swagger**",
            "/swagger-resources/**",
            "/admin/**",
            "/css/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
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
            .antMatchers("/images").permitAll()
            .antMatchers("/**").hasRole("USER")
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
            .successHandler(oAuth2AuthenticationSuccessHandler);
    }
}