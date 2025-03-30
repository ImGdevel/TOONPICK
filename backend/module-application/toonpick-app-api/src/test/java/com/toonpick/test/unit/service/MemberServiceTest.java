package com.toonpick.test.unit.service;

import com.toonpick.dto.MemberProfileDetailsResponseDTO;
import com.toonpick.dto.MemberProfileRequestDTO;
import com.toonpick.dto.MemberProfileResponseDTO;
import com.toonpick.dto.MemberResponseDTO;
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
import toonpick.test.domain.member.Member;
import toonpick.test.domain.member.MemberRole;
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
                .profilePicture("newProfileImageUrl")
                .build();

        profileDetailsResponseDTO = MemberProfileDetailsResponseDTO.builder()
                .username("testuser")
                .nickname("newNickname")
                .profilePicture("newProfileImageUrl")
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
        assertEquals("newProfileImageUrl", result.getProfilePicture());
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
        assertEquals("newProfileImageUrl", result.getProfilePicture());
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
                .profilePicture("newProfileImageUrl")
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
        assertEquals("newProfileImageUrl", result.getProfilePicture());
        assertEquals("testuser@example.com", result.getEmail());
        assertTrue(result.getIsAdultVerified());
        assertEquals(0, result.getLevel());
    }

}
