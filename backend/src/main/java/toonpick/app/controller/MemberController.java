package toonpick.app.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import toonpick.app.dto.member.MemberProfileDetailsResponseDTO;
import toonpick.app.dto.member.MemberProfileRequestDTO;
import toonpick.app.dto.member.MemberResponseDTO;
import toonpick.app.service.S3Service;
import toonpick.app.utils.AuthenticationUtil;
import toonpick.app.service.MemberService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/secure/member")
public class MemberController {

    private final MemberService memberService;
    private final AuthenticationUtil authenticationUtil;
    private final S3Service s3Service;

    private final Logger logger = LoggerFactory.getLogger(MemberController.class);

    // Member 프로필 조회
    @GetMapping("/profile")
    public ResponseEntity<MemberProfileDetailsResponseDTO> getUserProfile(Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);

        MemberProfileDetailsResponseDTO memberResponseDTO = memberService.getProfileDetails(username);
        return ResponseEntity.ok(memberResponseDTO);
    }

    // Member 프로필 업데이트
    @PutMapping("/profile")
    public ResponseEntity<Void> updateProfile(@RequestBody MemberProfileRequestDTO memberProfileRequestDTO, Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        memberService.updateProfile(username, memberProfileRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/profile-picture")
    public ResponseEntity<String> uploadProfilePicture(@RequestParam("profile") MultipartFile file, Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        String fileUrl = s3Service.uploadFile(file);
        memberService.updateProfilePicture(username, fileUrl);
        return ResponseEntity.ok(fileUrl);
    }

    // Member 패스워드 변경
    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestBody String newPassword, Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        memberService.changePassword(username, newPassword);
        return ResponseEntity.noContent().build();
    }

    // Member 성인 인증
    @PutMapping("/verify-adult")
    public ResponseEntity<Void> verifyAdult(Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        memberService.verifyAdult(username);
        return ResponseEntity.noContent().build();
    }

    // Member 조회
    @GetMapping("/details")
    public ResponseEntity<MemberResponseDTO> getMemberByUsername(Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);

        MemberResponseDTO memberResponseDTO = memberService.getMemberByUsername(username);
        return ResponseEntity.ok(memberResponseDTO);
    }

}
