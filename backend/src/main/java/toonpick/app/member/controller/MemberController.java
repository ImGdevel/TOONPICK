package toonpick.app.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toonpick.app.auth.user.CustomUserDetails;
import toonpick.app.member.dto.MemberDTO;
import toonpick.app.member.service.MemberService;

@RestController
@RequestMapping("/api/user")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/profile")
    public ResponseEntity<MemberDTO> getUserProfile(HttpServletRequest request, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        MemberDTO memberDTO = memberService.getMemberByUsername(username);
        return ResponseEntity.ok(memberDTO);
    }
}
