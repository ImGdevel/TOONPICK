package com.toonpick.auth.request;

import com.toonpick.constants.ValidationPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequest {

    @NotBlank(message = "아이디는 필수입니다")
    private String loginId;

    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 8, max = 30, message = "비밀번호는 8자 이상 30자 이하여야 합니다")
    @Pattern(regexp = ValidationPatterns.PASSWORD, message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다")
    private String password;
}
