package com.wekids.backend.auth.jwt;

import com.wekids.backend.exception.ErrorCode;
import com.wekids.backend.exception.ErrorResponse;
import com.wekids.backend.refreshToken.repository.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/logout$")) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {
            sendErrorResponse(response, HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Only POST method is allowed.");
            return;
        }

        String refresh = null;
        Cookie[] cookies = request.getCookies();

        if (cookies == null || cookies.length == 0) {
            sendErrorResponse(response, "No cookies found.");
            return;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            sendErrorResponse(response, "Refresh token not found.");
            return;
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            sendErrorResponse(response, "Refresh token is expired.");
            return;
        }

        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            sendErrorResponse(response, "Invalid token category.");
            return;
        }

        Boolean isExist = refreshTokenRepository.existsByToken(refresh);
        if (!isExist) {
            sendErrorResponse(response, "Refresh token does not exist.");
            return;
        }

        refreshTokenRepository.deleteByToken(refresh);

        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void sendErrorResponse(HttpServletResponse response,String message) throws IOException {
        ErrorCode errorCode = ErrorCode.BAD_REQUEST_COOKIE;

        response.setStatus(errorCode.getStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ErrorResponse errorResponse = ErrorResponse.createWithoutTimeStamp(errorCode, message);
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
