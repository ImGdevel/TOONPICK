package service;

import com.toonpick.dto.MemberProfileDetailsResponseDTO;
import com.toonpick.dto.MemberProfileRequestDTO;
import com.toonpick.dto.MemberProfileResponseDTO;
import com.toonpick.dto.MemberResponseDTO;
import com.toonpick.entity.Member;
import com.toonpick.enums.MemberRole;
import com.toonpick.mapper.MemberMapper;
import com.toonpick.repository.MemberRepository;
import com.toonpick.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.toonpick.exception.ResourceNotFoundException;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberMapper memberMapper;

    @InjectMocks
    private MemberService memberService;

    private Member member;
    private MemberProfileRequestDTO profileRequestDTO;
    private MemberProfileResponseDTO profileResponseDTO;
    private MemberProfileDetailsResponseDTO profileDetailsResponseDTO;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .username("testuser")
                .email("testuser@example.com")
                .role(MemberRole.ROLE_USER)
                .isAdultVerified(false)
                .build();

        profileRequestDTO = MemberProfileRequestDTO.builder()
                .nickname("newNickname")
                .build();

        profileResponseDTO = MemberProfileResponseDTO.builder()
                .username("testuser")
                .nickname("newNickname")
                .profileImage("newProfileImageUrl")
                .build();

        profileDetailsResponseDTO = MemberProfileDetailsResponseDTO.builder()
                .username("testuser")
                .nickname("newNickname")
                .profileImage("newProfileImageUrl")
                .email("testuser@example.com")
                .isAdultVerified(true)
                .level(0)
                .build();
    }

    @DisplayName("정상적인 사용자 프로필 조회 단위 테스트")
    @Test
    void testGetProfile_Success() {
        // given
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));
        when(memberMapper.memberToProfileResponseDTO(member)).thenReturn(profileResponseDTO);

        // when
        MemberProfileResponseDTO result = memberService.getProfile("testuser");

        // then
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("newNickname", result.getNickname());
        assertEquals("newProfileImageUrl", result.getProfileImage());
    }

    @DisplayName("존재하지 않는 사용자 프로필 조회 예외 단위 테스트")
    @Test
    void testGetProfile_Failure() {
        // given
        when(memberRepository.findByUsername("nonexistentuser")).thenReturn(Optional.empty());

        // when
        assertThrows(ResourceNotFoundException.class, () -> memberService.getProfile("nonexistentuser"));
    }

    @DisplayName("사용자 상세 프로필 조회 단위 테스트")
    @Test
    void testGetProfileDetails_Success() {
        // given
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));
        when(memberMapper.memberToProfileDetailsResponseDTO(member)).thenReturn(profileDetailsResponseDTO);

        // when
        MemberProfileDetailsResponseDTO result = memberService.getProfileDetails("testuser");

        // then
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("newNickname", result.getNickname());
        assertEquals("newProfileImageUrl", result.getProfileImage());
        assertEquals("testuser@example.com", result.getEmail());
        assertTrue(result.getIsAdultVerified());
        assertEquals(0, result.getLevel());
    }

    @DisplayName("사용자 프로필 수정 단위 테스트")
    @Test
    void testUpdateProfile_Success() {
        // given
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));

        // when
        memberService.updateProfile("testuser", profileRequestDTO);

        // then
        verify(memberRepository, times(1)).save(member);
        assertEquals("newNickname", member.getNickname());
    }

    @DisplayName("사용자 프로필 이미지 수정 단위 테스트")
    @Test
    void testUpdateProfileImage_Success() {
        // given
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));

        // when
        memberService.updateProfileImage("testuser", "newProfileImageUrl" );

        // then
        verify(memberRepository, times(1)).save(member);
        assertEquals("newProfileImageUrl", member.getProfileImage());
    }

    @DisplayName("사용자 패스워드 수정 단위 테스트")
    @Test
    void testChangePassword_Success() {
        // given
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));

        // when
        memberService.changePassword("testuser", "newPassword");

        // then
        verify(memberRepository, times(1)).save(member);
        assertEquals("newPassword", member.getPassword());
    }

    @DisplayName("사용자 성인 인증 단위 테스트")
    @Test
    void testVerifyAdult_Success() {
        // given
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));

        // when
        memberService.verifyAdult("testuser");

        // then
        verify(memberRepository, times(1)).save(member);
        assertTrue(member.getIsAdultVerified());
    }

    @DisplayName("사용자 정보 조회 테스트")
    @Test
    void testGetMemberByUsername_Success() {
        // given
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));
        when(memberMapper.memberToMemberResponseDTO(member)).thenReturn(MemberResponseDTO.builder()
                .username("testuser")
                .nickname("newNickname")
                .profileImage("newProfileImageUrl")
                .email("testuser@example.com")
                .isAdultVerified(true)
                .level(0)
                .build());

        // when
        MemberResponseDTO result = memberService.getMemberByUsername("testuser");

        // then
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("newNickname", result.getNickname());
        assertEquals("newProfileImageUrl", result.getProfileImage());
        assertEquals("testuser@example.com", result.getEmail());
        assertTrue(result.getIsAdultVerified());
        assertEquals(0, result.getLevel());
    }

    @DisplayName("로그인 성공 시 마지막 로그인 시간 설정 및 실패 카운트 초기화")
    @Test
    void testLoginSuccess() {
        Member member = Member.builder()
                .username("testuser")
                .email("test@example.com")
                .role(MemberRole.ROLE_USER)
                .build();

        member.login();

        assertNotNull(member.getLastLoginAt());
        assertEquals(0, member.getLoginFailCount());
    }

    @DisplayName("로그인 실패 시 실패 카운트 증가")
    @Test
    void testLoginFailIncreasesCount() {
        Member member = Member.builder().build();

        member.loginFail();
        member.loginFail();

        assertEquals(2, member.getLoginFailCount());
    }

    @DisplayName("로그인 실패 카운트 초기화")
    @Test
    void testResetLoginFailCount() {
        Member member = Member.builder().build();

        member.loginFail();
        member.resetLoginFailCount();

        assertEquals(0, member.getLoginFailCount());
    }

    @DisplayName("이메일 인증 처리")
    @Test
    void testVerifyEmail() {
        Member member = Member.builder().build();

        member.verifyEmail();

        assertTrue(member.isEmailVerified());
    }

    @DisplayName("개인정보 제공 동의 시 동의 시간 저장")
    @Test
    void testAgreeTerms() {
        Member member = Member.builder().build();

        member.agreeTerms();

        assertNotNull(member.getAgreedTermsAt());
    }

    @DisplayName("닉네임 변경")
    @Test
    void testUpdateProfile() {
        Member member = Member.builder().build();

        member.updateProfile("newNickname");

        assertEquals("newNickname", member.getNickname());
    }

    @DisplayName("프로필 이미지 변경")
    @Test
    void testUpdateProfileImage() {
        Member member = Member.builder().build();

        member.updateProfileImage("url");

        assertEquals("url", member.getProfileImage());
    }

    @DisplayName("비밀번호 변경")
    @Test
    void testChangePassword() {
        Member member = Member.builder().build();

        member.changePassword("newPassword");

        assertEquals("newPassword", member.getPassword());
    }

    @DisplayName("성인 인증 처리")
    @Test
    void testVerifyAdult() {
        Member member = Member.builder().build();

        member.verifyAdult();

        assertTrue(member.getIsAdultVerified());
    }

    @DisplayName("계정 활성화 처리")
    @Test
    void testActivateAccount() {
        Member member = Member.builder().build();

        member.activateAccount();

        assertEquals("ACTIVE", member.getStatus().name());
    }

    @DisplayName("휴면 계정 처리")
    @Test
    void testSuspendAccount() {
        Member member = Member.builder().build();

        member.suspendAccount();

        assertEquals("SUSPENDED", member.getStatus().name());
    }

    @DisplayName("계정 삭제 처리")
    @Test
    void testDeactivateAccount() {
        Member member = Member.builder().build();

        member.deactivateAccount();

        assertEquals("DELETED", member.getStatus().name());
    }

    @DisplayName("레벨 업데이트 처리")
    @Test
    void testUpdateLevel() {
        Member member = Member.builder().build();

        member.updateLevel(5);

        assertEquals(5, member.getLevel());
    }

}
