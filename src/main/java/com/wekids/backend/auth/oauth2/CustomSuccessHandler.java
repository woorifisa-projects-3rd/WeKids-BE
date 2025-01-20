package com.wekids.backend.auth.oauth2;

import com.wekids.backend.auth.dto.response.CustomOAuth2User;
import com.wekids.backend.auth.enums.LoginState;
import com.wekids.backend.auth.jwt.JWTUtil;
import com.wekids.backend.exception.ErrorCode;
import com.wekids.backend.exception.WekidsException;
import com.wekids.backend.member.domain.Member;
import com.wekids.backend.member.repository.MemberRepository;
import com.wekids.backend.refreshToken.domain.RefreshToken;
import com.wekids.backend.refreshToken.repository.RefreshTokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;
    @Value("${client.url}")
    private String CLIENT_URL;

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
        LoginState loginState = customUserDetails.getLoginState();

        if(loginState.equals(LoginState.JOIN)){
            response.addCookie(createCookie("name", customUserDetails.getName()));
            response.addCookie(createCookie("email", customUserDetails.getEmail()));
            response.addCookie(createCookie("birthday", customUserDetails.getBirthday()));
            response.sendRedirect(CLIENT_URL+"/signup/select");
            return;
        }

        String access = jwtUtil.createJwt("access", customUserDetails.getMemberId(), customUserDetails.getRole());
        String refresh = jwtUtil.createJwt("refresh", customUserDetails.getMemberId(), customUserDetails.getRole());

        saveRefreshToken(refresh, customUserDetails.getMemberId());

        response.addCookie(createCookie("access", access));
        response.addCookie(createCookie("refresh", refresh));
        response.sendRedirect(CLIENT_URL);

    }

    private void saveRefreshToken(String token, Long memberId){
        Member member = findMemberByMemberId(memberId);
        RefreshToken refresh = RefreshToken.of(token, member, jwtUtil.getExpirationTime("refresh"));
        refreshTokenRepository.save(refresh);
    }

    private Member findMemberByMemberId(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(()->new RuntimeException("Member Not Find: " + memberId));
    }

    private Cookie createCookie(String key, String value){
        try {
            String encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
            Cookie cookie = new Cookie(key, encodedValue);
            cookie.setMaxAge(60 * 60 * 60);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setSecure(true); // HTTPS에서만 설정
            return cookie;
        }
        catch (Exception e){
            throw new WekidsException(ErrorCode.FAILED_COOKIE_ENCODING, "쿠키 인코딩 에러");
        }
    }
}
