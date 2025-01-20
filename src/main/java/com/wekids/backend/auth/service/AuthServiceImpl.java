package com.wekids.backend.auth.service;

import com.wekids.backend.auth.dto.request.SignUpRequest;
import com.wekids.backend.auth.enums.MemberType;
import com.wekids.backend.auth.jwt.JWTUtil;
import com.wekids.backend.exception.ErrorCode;
import com.wekids.backend.exception.WekidsException;
import com.wekids.backend.member.domain.Child;
import com.wekids.backend.member.domain.Member;
import com.wekids.backend.member.domain.Parent;
import com.wekids.backend.member.domain.mapping.ParentChild;
import com.wekids.backend.member.repository.MemberRepository;
import com.wekids.backend.member.repository.ParentChildRepository;
import com.wekids.backend.member.repository.ParentRepository;
import com.wekids.backend.refreshToken.domain.RefreshToken;
import com.wekids.backend.refreshToken.repository.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;

import static com.wekids.backend.exception.ErrorCode.EMAIL_ALREADY_EXIST;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService{
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ParentChildRepository parentChildRepository;
    private final ParentRepository parentRepository;
    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void signup(SignUpRequest signUpRequest, HttpServletRequest request, HttpServletResponse response) {
        validateAlreadyExistEmail(signUpRequest);

        Long memberId = signUpRequest.getMemberType().equals(MemberType.PARENT) ? signupParent(signUpRequest) : signupChild(signUpRequest);

        String access = jwtUtil.createJwt("access", memberId, "ROLE_" + signUpRequest.getMemberType());
        String refresh = jwtUtil.createJwt("refresh", memberId, "ROLE_" + signUpRequest.getMemberType());

        Member member = findMemberByMemberId(memberId);
        refreshTokenRepository.save(RefreshToken.of(refresh, member, jwtUtil.getExpirationTime("refresh")));

        response.setHeader("access", access);
        response.addCookie(createCookie("refresh", refresh));
    }

    private void validateAlreadyExistEmail(SignUpRequest request){
        if(memberRepository.existsAllByEmail(request.getEmail())){
            throw new WekidsException(EMAIL_ALREADY_EXIST, request.getEmail() + "은 존재합니다");
        }
    }

    private Long signupParent(SignUpRequest request){
        validateAdult(request.getBirthday());
        validateSimplePassword(request);

        String encode = passwordEncoder.encode(request.getSimplePassword());
        Parent parent = request.toParent(encode, request);

        return memberRepository.save(parent).getId();
    }

    private void validateAdult(LocalDate birthdate) {
        LocalDate today = LocalDate.now();
        Period age = Period.between(birthdate, today);
        if (age.getYears() < 19) {
            throw new WekidsException(ErrorCode.INVALID_SIGNUP_AGE, "현재 나이는 " + age.getYears() + "입니다. 19세 이상이어야 합니다");
        }
    }

    private void validateSimplePassword(SignUpRequest request){
        if(request.getSimplePassword() == null){
            throw new WekidsException(ErrorCode.INVALID_INPUT, request.getEmail() + "님은 간편 비밀번호가 없습니다");
        }
    }


    private Long signupChild(SignUpRequest request){
        validateUnderFourteen(request.getBirthday());

        Parent parent = findParentByPhoneAndName(request.getGuardianPhone(), request.getGuardianName());
        Child child = request.toChild(request);
        ParentChild parentChild = ParentChild.of(parent, child);

        Long childId = memberRepository.save(child).getId();
        parentChildRepository.save(parentChild);

        return childId;
    }

    public void validateUnderFourteen(LocalDate birthdate) {
        LocalDate today = LocalDate.now();
        Period age = Period.between(birthdate, today);
        if (age.getYears() >= 14) {
            throw new WekidsException(ErrorCode.INVALID_SIGNUP_AGE, "현재 나이는 " + age.getYears() + "입니다. 14세 미만이어야 합니다.");
        }
    }

    private Parent findParentByPhoneAndName(String phone, String name){
        return parentRepository.findByPhoneAndName(phone, name).orElseThrow(()
                -> new WekidsException(ErrorCode.MEMBER_NOT_FOUND, phone + "의 전화번호를 가진 법정대리인은 Wekids에 부모 계정으로 회원가입하지 않았습니다."));
    }

    @Override
    public void reissue(HttpServletRequest request, HttpServletResponse response) {
        String beforeRefresh = findRefreshToken(request, response);

        Long memberId = jwtUtil.getMemberId(beforeRefresh);
        String role = jwtUtil.getRole(beforeRefresh);

        String access = jwtUtil.createJwt("access", memberId, role);
        String refresh = jwtUtil.createJwt("refresh", memberId, role);

        refreshTokenRepository.deleteByToken(beforeRefresh);

        Member member = findMemberByMemberId(memberId);
        refreshTokenRepository.save(RefreshToken.of(refresh, member, jwtUtil.getExpirationTime("refresh")));

        response.setHeader("access", access);
        response.addCookie(createCookie("refresh", refresh));
    }

    private String findRefreshToken(HttpServletRequest request, HttpServletResponse response){
        String refresh = null;
        Cookie[] cookies = request.getCookies();

        if (cookies == null || cookies.length == 0) {
            throw new WekidsException(ErrorCode.NOT_FIND_COOKIE, "요청한 쿠기가 없습니다.");
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            throw new WekidsException(ErrorCode.NOT_FIND_COOKIE, "요청한 쿠키 중 refresh를 찾을 수 없습니다.");
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new WekidsException(ErrorCode.EXPIRE_TOKEN, "만료된 Refresh 토큰입니다.");
        }

        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            throw new WekidsException(ErrorCode.INVALID_TOKEN_CATEGORY, "해당 토큰은 " + category + " 유형의 토큰입니다.");
        }

        Boolean isExist = refreshTokenRepository.existsByToken(refresh);
        if (!isExist) {
            throw new WekidsException(ErrorCode.NOT_FIND_TOKEN, "서버에 DB에 없는 토큰입니다.");
        }

        return refresh;
    }

    private Cookie createCookie(String key, String value){
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        return cookie;
    }

    private Member findMemberByMemberId(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(()->new WekidsException(ErrorCode.MEMBER_NOT_FOUND, memberId + "을 찾을 수 없습니다."));
    }
}
