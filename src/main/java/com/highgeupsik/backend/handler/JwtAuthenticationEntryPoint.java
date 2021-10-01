package com.highgeupsik.backend.handler;

import com.highgeupsik.backend.utils.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.highgeupsik.backend.utils.ApiUtils.error;
import static com.highgeupsik.backend.utils.ErrorMessage.*;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint { //인증 정보 없을때 401 UNAUTHORIZED

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(this.objectMapper.writeValueAsString(
                (error(new ApiError(NOT_USER, HttpStatus.UNAUTHORIZED)))));
    }
}
