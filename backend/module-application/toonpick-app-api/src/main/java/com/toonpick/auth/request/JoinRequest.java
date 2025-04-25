package com.toonpick.auth.request;

import com.toonpick.constants.ValidationPatterns;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinRequest {

    @NotBlank(message = "아이디는 필수입니다")
    private String loginId;

    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "이메일 형식이 올바르지 않습니다")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, max = 30, message = "비밀번호는 8자 이상 30자 이하여야 합니다")
    @Pattern(regexp = ValidationPatterns.PASSWORD, message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다")
    private String password;

    @AssertTrue(message = "이메일 인증해야 합니다")
    private Boolean emailVerified;

    @AssertTrue(message = "이용약관에 동의해야 합니다")
    private Boolean termsAgreed;

    @AssertTrue(message = "개인정보 처리방침에 동의해야 합니다")
    private Boolean privacyPolicyAgreed;
}
