package com.toonpick.auth.controller;

import com.toonpick.internal.security.dto.JoinRequest;
import com.toonpick.auth.service.MemberJoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Tag(name = "Join", description = "회원가입 (접근 권한 : Private)")
@Controller
@ResponseBody
@RequiredArgsConstructor
public class JoinController {

    private final MemberJoinService memberJoinService;

    @Operation(summary = "회원가입")
    @PostMapping("/join")
    public ResponseEntity<?> joinProcess(
            @Parameter(description = "회원가입 포맷")
            @RequestBody @Valid JoinRequest joinRequest
    ) {
        memberJoinService.registerMember(joinRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
