package com.wekids.backend.auth.controller;

import com.wekids.backend.auth.dto.request.SignUpRequest;
import com.wekids.backend.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/api/v1/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignUpRequest signUpRequest, HttpServletRequest request, HttpServletResponse response){
        authService.signup(signUpRequest, request, response);

        return new ResponseEntity<>(CREATED);
    }

    @PostMapping("/reissue")
    public ResponseEntity<Void> reissue(HttpServletRequest request, HttpServletResponse response){
        authService.reissue(request, response);

        return new ResponseEntity<>(CREATED);
    }
}
