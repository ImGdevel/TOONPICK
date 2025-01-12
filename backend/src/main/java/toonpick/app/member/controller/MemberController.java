package toonpick.app.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toonpick.app.common.utils.AuthenticationUtil;
import toonpick.app.member.dto.MemberDTO;
import toonpick.app.member.service.MemberService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/secure/member")
public class MemberController {

    private final MemberService memberService;
    private final AuthenticationUtil authenticationUtil;

    @GetMapping("/profile")
    public ResponseEntity<MemberDTO> getUserProfile(HttpServletRequest request, Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);

        MemberDTO memberDTO = memberService.getMemberByUsername(username);
        return ResponseEntity.ok(memberDTO);
    }
}
