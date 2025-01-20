package com.wekids.backend.auth.service;

import com.wekids.backend.auth.dto.request.SignUpRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    void signup(SignUpRequest signUpRequest, HttpServletRequest request, HttpServletResponse response);
    void reissue(HttpServletRequest request, HttpServletResponse response);
}
