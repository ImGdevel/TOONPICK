package com.toonpick.auth.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class JoinValidationException extends MethodArgumentNotValidException {

    public JoinValidationException(MethodParameter parameter, BindingResult bindingResult) {
        super(parameter, bindingResult);
    }

    public String getFirstErrorMessage() {
        return getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("잘못된 입력입니다");
    }
}
