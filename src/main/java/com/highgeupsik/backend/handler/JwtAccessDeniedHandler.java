package com.highgeupsik.backend.handler;

import static com.highgeupsik.backend.api.ApiUtils.error;
import static com.highgeupsik.backend.utils.ErrorMessage.NOT_ROLE_USER;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highgeupsik.backend.api.ApiError;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(this.objectMapper
            .writeValueAsString((error(new ApiError("FORBIDDEN", NOT_ROLE_USER)))));
    }
}
