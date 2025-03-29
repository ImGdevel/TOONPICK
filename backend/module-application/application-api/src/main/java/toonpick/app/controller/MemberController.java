package toonpick.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import toonpick.dto.MemberProfileDetailsResponseDTO;
import toonpick.dto.MemberProfileRequestDTO;
import toonpick.dto.MemberResponseDTO;
import toonpick.app.infra.aws.s3.S3Service;
import toonpick.app.utils.AuthenticationUtil;
import toonpick.service.MemberService;

@Tag(name = "Member", description = "회원 관련 API (접근 권한 : Private)")
@RestController
@RequestMapping("/api/secure/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AuthenticationUtil authenticationUtil;
    private final S3Service s3Service;

    private final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Operation(summary = "회원 정보 조회", description = "현재 인증된 사용자의 기본 정보를 조회합니다")
    @GetMapping
    public ResponseEntity<MemberResponseDTO> getMemberByUsername(Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);

        MemberResponseDTO memberResponseDTO = memberService.getMemberByUsername(username);
        return ResponseEntity.ok(memberResponseDTO);
    }

    @Operation(summary = "회원 상세 프로필 조회", description = "현재 인증된 사용자의 상세 프로필 정보를 조회합니다")
    @GetMapping("/profile")
    public ResponseEntity<MemberProfileDetailsResponseDTO> getUserProfile(Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);

        MemberProfileDetailsResponseDTO memberResponseDTO = memberService.getProfileDetails(username);
        return ResponseEntity.ok(memberResponseDTO);
    }

    @Operation(summary = "회원 프로필 업데이트", description = "현재 인증된 사용자의 프로필 정보를 업데이트합니다")
    @PutMapping("/profile")
    public ResponseEntity<Void> updateProfile(
            @Parameter(description = "회원 프로필 수정 요청 양식 (닉네임 등)", required = true)
            @RequestBody MemberProfileRequestDTO memberProfileRequestDTO,
            Authentication authentication
    ) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        memberService.updateProfile(username, memberProfileRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "회원 프로필 이미지 업로드", description = "회원의 프로필 이미지를 업로드합니다")
    @PostMapping("/profile-image")
    public ResponseEntity<String> uploadProfilePicture(
            @Parameter(description = "업로드할 이미지 파일 (JPEG, PNG 등 허용)", required = true)
            @RequestParam("image") MultipartFile file,
            Authentication authentication
    ) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        String fileUrl = s3Service.uploadFile(file);
        memberService.updateProfileImage(username, fileUrl);
        return ResponseEntity.ok(fileUrl);
    }

    // todo : 별도의 Controller 로 분리 가능성 검토 할 것
    @Operation(summary = "회원 패스워드 변경", description = "회원의 패스워드를 변경합니다")
    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(
            @Parameter(description = "새로운 패스워드", required = true)
            @RequestBody String newPassword,
            Authentication authentication
    ) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        memberService.changePassword(username, newPassword);
        return ResponseEntity.noContent().build();
    }

    // todo : 회원 성인 인증 로직 구현
    @Operation(summary = "회원 성인인증", description = "회원의 성인 인증 상태를 업데이트합니다. 해당 인증은 추가적인 권한을 필요로 할 수 있습니다")
    @PutMapping("/verify-adult")
    public ResponseEntity<Void> verifyAdult(Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        memberService.verifyAdult(username);
        return ResponseEntity.noContent().build();
    }

}
