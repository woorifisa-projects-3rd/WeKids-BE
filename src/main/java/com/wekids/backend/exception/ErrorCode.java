package com.wekids.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorCode {
    INVALID_INPUT(INTERNAL_SERVER_ERROR, "잘못된 입력 값입니다."),
    MEMBER_NOT_FOUND(INTERNAL_SERVER_ERROR, "회원을 찾을 수 없습니다."),
    CARD_NOT_FOUND(INTERNAL_SERVER_ERROR, "카드를 찾을 수 없습니다."),
    TRANSACTION_NOT_FOUND(INTERNAL_SERVER_ERROR, "거래 내역을 찾을 수 없습니다."),
    INVALID_TRANSACTION_AMOUNT(INTERNAL_SERVER_ERROR, "거래 할 수 없는 금액입니다."),
    INVALID_ACCOUNT_NUMBER(INTERNAL_SERVER_ERROR, "계좌번호를 찾을 수 없습니다."),
    ACCOUNT_NOT_ACTIVE(INTERNAL_SERVER_ERROR, "정지된 계좌입니다."),
    ACCOUNT_NOT_FOUND(INTERNAL_SERVER_ERROR, "계좌를 찾을 수 없습니다."),
    BAAS_NOT_RESPONSE(GATEWAY_TIMEOUT, "BaaS 응답이 없습니다."),
    BAAS_REQUEST_FAILED(INTERNAL_SERVER_ERROR, "BaaS 요청에 실패했습니다."),
    DESIGN_NOT_FOUND(INTERNAL_SERVER_ERROR, "디자인 정보를 찾을 수 없습니다."),
    INVALID_CARD_STATE(INTERNAL_SERVER_ERROR, "잘못된 카드 상태입니다."),
    NOT_REGISTERED_BANK_MEMBER(INTERNAL_SERVER_ERROR, "아직 등록되지 않은 은행 회원입니다."),
    EMAIL_ALREADY_EXIST(CONFLICT, "이미 존재하는 이메일입니다."),
    INVALID_SIGNUP_AGE(INTERNAL_SERVER_ERROR, "회원가입할 수 없는 나이입니다."),
    FAILED_COOKIE_ENCODING(INTERNAL_SERVER_ERROR, "쿠키 인코딩에 실패했습니다."),
    FAILED_READ_LOG(INTERNAL_SERVER_ERROR, "로그 읽기에 실패했습니다."),
    FAILED_DELETE_LOG(INTERNAL_SERVER_ERROR, "로그 삭제에 실패했습니다."),
    FAILED_SAVE_LOG(INTERNAL_SERVER_ERROR, "로그 저장에 실패했습니다."),
    FAILED_SAVE_IMAGE(INTERNAL_SERVER_ERROR, "이미지 저장에 실패했습니다."),
    FAILED_DELETE_IMAGE(INTERNAL_SERVER_ERROR, "이미지 삭제에 실패했습니다."),
    INVALID_MEMBER_RELATIONSHIP(INTERNAL_SERVER_ERROR, "부모 자식 관계가 아닙니다."),
    MISSION_NOT_FOUND(INTERNAL_SERVER_ERROR, "미션을 찾을 수 없습니다."),
    NOT_ALLOWED_MEMBER_ACCESS(INTERNAL_SERVER_ERROR, "허용되지 않은 사용자가 접근했습니다."),
    NOT_MATCHED_PASSWORD(INTERNAL_SERVER_ERROR, "간편 비밀번호가 일치하지 않습니다."),
    FAILED_UPDATE_PASSWORD(INTERNAL_SERVER_ERROR, "비밀번호 변경에 실패했습니다"),
    RELATIONSHIP_NOT_FOUND(INTERNAL_SERVER_ERROR, "존재하지 않는 관계입니다."),
    ALARM_NOT_FOUND(INTERNAL_SERVER_ERROR, "존재하지 않은 알림입니다."),
    BAD_REQUEST_COOKIE(BAD_REQUEST, "요청한 쿠키가 잘 못 되었습니다.");


    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
