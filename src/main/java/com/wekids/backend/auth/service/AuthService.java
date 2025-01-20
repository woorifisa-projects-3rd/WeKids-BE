package com.wekids.backend.auth.service;

import com.wekids.backend.auth.dto.request.SignUpRequest;
import com.wekids.backend.auth.dto.response.SignUpResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    void signup(SignUpRequest signUpRequest);
    void reissue(HttpServletRequest request, HttpServletResponse response);
}
