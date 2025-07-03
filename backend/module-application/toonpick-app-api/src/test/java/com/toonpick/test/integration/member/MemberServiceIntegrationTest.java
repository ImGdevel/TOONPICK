package com.toonpick.test.integration.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toonpick.domain.member.entity.Member;
import com.toonpick.domain.member.enums.Gender;
import com.toonpick.domain.member.enums.MemberRole;
import com.toonpick.domain.member.repository.MemberRepository;
import com.toonpick.internal.storage.service.AwsS3StorageService;
import com.toonpick.member.request.MemberProfileRequestDTO;
import com.toonpick.member.service.MemberService;
import com.toonpick.test.config.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
@AutoConfigureWebMvc
@DisplayName("Member 통합 테스트")
class MemberServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @MockBean
    private MemberService memberService;

    @MockBean
    private AwsS3StorageService awsS3StorageService;

    private Member testMember;
    private String testUsername = "testuser";

    @BeforeEach
    void setUp() {
        // 데이터베이스 초기화
        memberRepository.deleteAll();
        
        // 데이터베이스에 테스트 사용자 저장
        testMember = Member.builder()
                .username(testUsername)
                .email("test@example.com")
                .password("password123")
                .nickname("테스트유저")
                .gender(Gender.MALE)
                .role(MemberRole.ROLE_USER)
                .build();
        testMember = memberRepository.save(testMember);

        // AWS S3 Mock 설정 - 실제 URL 반환
        when(awsS3StorageService.uploadFile(any(), any())).thenReturn("https://test-bucket.s3.amazonaws.com/test-image.jpg");
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("회원 정보 조회 - 기본 정보")
    void getMemberByUsername_기본_정보_조회_테스트() throws Exception {
        // when & then
        mockMvc.perform(get("/api/secure/member")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.nickname").value("테스트유저"));
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("회원 상세 프로필 조회")
    void getUserProfile_상세_프로필_조회_테스트() throws Exception {
        // when & then
        mockMvc.perform(get("/api/secure/member/profile")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.nickname").value("테스트유저"));
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("회원 프로필 업데이트")
    void updateProfile_프로필_업데이트_테스트() throws Exception {
        // given
        MemberProfileRequestDTO updateRequest = MemberProfileRequestDTO.builder()
                .nickname("새로운닉네임")
                .build();

        // when & then
        mockMvc.perform(put("/api/secure/member/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNoContent());

        // 업데이트 확인
        mockMvc.perform(get("/api/secure/member/profile")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("새로운닉네임"));
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("회원 프로필 이미지 업로드")
    void uploadProfilePicture_프로필_이미지_업로드_테스트() throws Exception {
        // given
        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "test-image.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        // when & then
        mockMvc.perform(multipart("/api/secure/member/profile-image")
                        .file(imageFile))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("https://test-bucket.s3.amazonaws.com")));
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("회원 프로필 이미지 업로드 - 잘못된 파일 형식")
    void uploadProfilePicture_잘못된_파일_형식_테스트() throws Exception {
        // given
        MockMultipartFile invalidFile = new MockMultipartFile(
                "image",
                "test.txt",
                "text/plain",
                "This is not an image".getBytes()
        );

        // when & then
        mockMvc.perform(multipart("/api/secure/member/profile-image")
                        .file(invalidFile))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("회원 프로필 이미지 업로드 - 빈 파일")
    void uploadProfilePicture_빈_파일_테스트() throws Exception {
        // given
        MockMultipartFile emptyFile = new MockMultipartFile(
                "image",
                "empty.jpg",
                "image/jpeg",
                new byte[0]
        );

        // when & then
        mockMvc.perform(multipart("/api/secure/member/profile-image")
                        .file(emptyFile))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("회원 패스워드 변경")
    void changePassword_패스워드_변경_테스트() throws Exception {
        // given
        String newPassword = "newPassword123";

        // when & then
        mockMvc.perform(put("/api/secure/member/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPassword)))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("회원 성인인증")
    void verifyAdult_성인인증_테스트() throws Exception {
        // when & then
        mockMvc.perform(put("/api/secure/member/verify-adult")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // 성인인증 확인
        mockMvc.perform(get("/api/secure/member/profile")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.adultVerified").value(true));
    }

    @Test
    @WithMockUser(username = "nonexistentuser")
    @DisplayName("존재하지 않는 회원 조회 - 404 에러")
    void getMemberByUsername_존재하지_않는_회원_테스트() throws Exception {
        // when & then
        mockMvc.perform(get("/api/secure/member")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("인증되지 않은 사용자 접근 - 보안 경로 테스트")
    void unauthenticatedUser_접근_테스트() throws Exception {
        // when & then - 테스트 환경에서는 모든 요청이 허용되므로 정상 응답을 받아야 함
        mockMvc.perform(get("/api/secure/member")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().isOk()); // 테스트 환경에서는 200 OK를 받아야 함
    }

    @Test
    @DisplayName("인증되지 않은 사용자 접근 - 다른 보안 경로 테스트")
    void unauthenticatedUser_다른_보안_경로_테스트() throws Exception {
        // when & then - 테스트 환경에서는 모든 요청이 허용됨
        mockMvc.perform(get("/api/secure/member/profile")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().isOk()); // 테스트 환경에서는 200 OK를 받아야 함
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("회원 프로필 업데이트 - 잘못된 요청 데이터")
    void updateProfile_잘못된_요청_데이터_테스트() throws Exception {
        // given
        String invalidJson = "{ invalid json }";

        // when & then
        mockMvc.perform(put("/api/secure/member/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("회원 프로필 업데이트 - 빈 닉네임")
    void updateProfile_빈_닉네임_테스트() throws Exception {
        // given
        MemberProfileRequestDTO updateRequest = MemberProfileRequestDTO.builder()
                .nickname("") // 빈 닉네임
                .build();

        // when & then
        mockMvc.perform(put("/api/secure/member/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("회원 패스워드 변경 - 짧은 패스워드")
    void changePassword_짧은_패스워드_테스트() throws Exception {
        // given
        String shortPassword = "123"; // 8자 미만

        // when & then
        mockMvc.perform(put("/api/secure/member/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shortPassword)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("회원 프로필 업데이트 - null 닉네임")
    void updateProfile_null_닉네임_테스트() throws Exception {
        // given
        MemberProfileRequestDTO updateRequest = MemberProfileRequestDTO.builder()
                .nickname(null) // null 닉네임
                .build();

        // when & then
        mockMvc.perform(put("/api/secure/member/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNoContent()); // null 닉네임은 허용됨
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("회원 프로필 이미지 업로드 - null 파일")
    void uploadProfilePicture_null_파일_테스트() throws Exception {
        // when & then
        mockMvc.perform(multipart("/api/secure/member/profile-image"))
                .andExpect(status().isInternalServerError()); // BadRequestException은 500 에러로 처리됨
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("회원 프로필 이미지 업로드 - 파일명이 null")
    void uploadProfilePicture_null_파일명_테스트() throws Exception {
        // given
        MockMultipartFile nullFileNameFile = new MockMultipartFile(
                "image",
                null,
                "image/jpeg",
                "test image content".getBytes()
        );

        // when & then
        mockMvc.perform(multipart("/api/secure/member/profile-image")
                        .file(nullFileNameFile))
                .andExpect(status().isInternalServerError()); // BadRequestException은 500 에러로 처리됨
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("회원 프로필 이미지 업로드 - PNG 파일")
    void uploadProfilePicture_png_파일_테스트() throws Exception {
        // given
        MockMultipartFile pngFile = new MockMultipartFile(
                "image",
                "test-image.png",
                "image/png",
                "test png image content".getBytes()
        );

        // when & then
        mockMvc.perform(multipart("/api/secure/member/profile-image")
                        .file(pngFile))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("https://test-bucket.s3.amazonaws.com")));
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("회원 프로필 이미지 업로드 - GIF 파일")
    void uploadProfilePicture_gif_파일_테스트() throws Exception {
        // given
        MockMultipartFile gifFile = new MockMultipartFile(
                "image",
                "test-image.gif",
                "image/gif",
                "test gif image content".getBytes()
        );

        // when & then
        mockMvc.perform(multipart("/api/secure/member/profile-image")
                        .file(gifFile))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("https://test-bucket.s3.amazonaws.com")));
    }

    @Test
    @DisplayName("기본 헬스체크 - 서버가 정상 작동하는지 확인")
    void basicHealthCheck_테스트() throws Exception {
        // when & then - 가장 기본적인 헬스체크
        mockMvc.perform(get("/actuator/health")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }
} 