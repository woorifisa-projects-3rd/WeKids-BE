package com.wekids.backend.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private String errorCode;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String details;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private LocalDateTime timestamp;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ValidErrorResponse> errors;


    public static ErrorResponse createWithoutTimeStamp(ErrorCode errorCode, String message) {
        return ErrorResponse.builder()
                .errorCode(errorCode.toString())
                .message(message)
                .build();
    }

    public static ErrorResponse of(String errorCode, String message) {
        return ErrorResponse.builder()
                .errorCode(errorCode)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }


    public static ErrorResponse of(ErrorCode errorCode, String details) {
        return ErrorResponse.builder()
                .errorCode(errorCode.toString())
                .message(errorCode.getMessage())
                .details(details)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ErrorResponse of(List<FieldError> errors, ErrorCode errorCode) {
        return ErrorResponse.builder()
                .errorCode(errorCode.toString())
                .message(errorCode.getMessage())
                .timestamp(LocalDateTime.now())
                .errors(errors.stream().map(ValidErrorResponse::from).toList())
                .build();
    }
}