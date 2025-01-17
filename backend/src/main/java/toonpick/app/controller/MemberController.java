package toonpick.app.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toonpick.app.dto.member.MemberProfileDetailsResponseDTO;
import toonpick.app.utils.AuthenticationUtil;
import toonpick.app.service.MemberService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/secure/member")
public class MemberController {

    private final MemberService memberService;
    private final AuthenticationUtil authenticationUtil;

    @GetMapping("/profile")
    public ResponseEntity<MemberProfileDetailsResponseDTO> getUserProfile(HttpServletRequest request, Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);

        MemberProfileDetailsResponseDTO memberResponseDTO = memberService.getProfileDetails(username);
        return ResponseEntity.ok(memberResponseDTO);
    }
}
