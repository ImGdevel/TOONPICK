package com.toonpick.dto;

import com.toonpick.type.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private final String code;
    private final String message;
    private final String timestamp;

    public ErrorResponse(ErrorCode errorCode){
        this.code = errorCode.name();
        this.message = errorCode.getMessage();
        this.timestamp = Instant.now().toString();
    }
}
