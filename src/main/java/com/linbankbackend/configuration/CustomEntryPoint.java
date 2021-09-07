package com.linbankbackend.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        Map<String, Object> error = new HashMap<>();
        error.put("code", 401);
        error.put("message", "Invalid credentials.");

        ObjectMapper mapper = new ObjectMapper();
        String responseMsg = mapper.writeValueAsString(error);
        response.getWriter().write(responseMsg);
    }

//    @ExceptionHandler(value = {AccessDeniedException.class})
//    public void commence(HttpServletRequest request, HttpServletResponse response,
//                         AccessDeniedException accessDeniedException) throws IOException {
//        // 401
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization Failed : " + accessDeniedException.getMessage());
//    }
//
////    IllegalStateException
//
//    @ExceptionHandler(value = {IllegalStateException.class})
//    public void commence(HttpServletRequest request, HttpServletResponse response,
//                         IllegalStateException illegalStateException ) throws IOException {
//        // 401
//        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Authorization Failed : " + illegalStateException.getMessage());
//    }
}
