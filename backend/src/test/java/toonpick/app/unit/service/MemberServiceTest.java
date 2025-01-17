package toonpick.app.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import toonpick.app.domain.member.Member;
import toonpick.app.domain.member.MemberRole;
import toonpick.app.dto.member.MemberProfileDetailsResponseDTO;
import toonpick.app.dto.member.MemberProfileRequestDTO;
import toonpick.app.dto.member.MemberProfileResponseDTO;
import toonpick.app.dto.member.MemberResponseDTO;
import toonpick.app.mapper.MemberMapper;
import toonpick.app.repository.MemberRepository;
import toonpick.app.service.MemberService;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

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
                .profilePicture("newProfilePicture")
                .build();

        profileResponseDTO = MemberProfileResponseDTO.builder()
                .username("testuser")
                .nickname("newNickname")
                .profilePicture("newProfilePicture")
                .build();

        profileDetailsResponseDTO = MemberProfileDetailsResponseDTO.builder()
                .username("testuser")
                .nickname("newNickname")
                .profilePicture("newProfilePicture")
                .email("testuser@example.com")
                .isAdultVerified(true)
                .level("VIP")
                .build();
    }

    @Test
    void testGetProfile_Success() {
        // Arrange
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));
        when(memberMapper.memberToProfileResponseDTO(member)).thenReturn(profileResponseDTO);

        // Act
        MemberProfileResponseDTO result = memberService.getProfile("testuser");

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("newNickname", result.getNickname());
        assertEquals("newProfilePicture", result.getProfilePicture());
    }

    @Test
    void testGetProfile_Failure() {
        // Arrange
        when(memberRepository.findByUsername("nonexistentuser")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> memberService.getProfile("nonexistentuser"));
    }

    @Test
    void testGetProfileDetails_Success() {
        // Arrange
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));
        when(memberMapper.memberToProfileDetailsResponseDTO(member)).thenReturn(profileDetailsResponseDTO);

        // Act
        MemberProfileDetailsResponseDTO result = memberService.getProfileDetails("testuser");

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("newNickname", result.getNickname());
        assertEquals("newProfilePicture", result.getProfilePicture());
        assertEquals("testuser@example.com", result.getEmail());
        assertTrue(result.getIsAdultVerified());
        assertEquals("VIP", result.getLevel());
    }

    @Test
    void testUpdateProfile_Success() {
        // Arrange
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));

        // Act
        memberService.updateProfile("testuser", profileRequestDTO);

        // Assert
        verify(memberRepository, times(1)).save(member);
        assertEquals("newNickname", member.getNickname());
        assertEquals("newProfilePicture", member.getProfilePicture());
    }

    @Test
    void testChangePassword_Success() {
        // Arrange
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));

        // Act
        memberService.changePassword("testuser", "newPassword");

        // Assert
        verify(memberRepository, times(1)).save(member);
        assertEquals("newPassword", member.getPassword());
    }

    @Test
    void testVerifyAdult_Success() {
        // Arrange
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));

        // Act
        memberService.verifyAdult("testuser");

        // Assert
        verify(memberRepository, times(1)).save(member);
        assertTrue(member.getIsAdultVerified());
    }

    @Test
    void testGetMemberByUsername_Success() {
        // Arrange
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));
        when(memberMapper.memberToMemberResponseDTO(member)).thenReturn(MemberResponseDTO.builder()
                .username("testuser")
                .nickname("newNickname")
                .profilePicture("newProfilePicture")
                .email("testuser@example.com")
                .isAdultVerified(true)
                .level("VIP")
                .build());

        // Act
        MemberResponseDTO result = memberService.getMemberByUsername("testuser");

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("newNickname", result.getNickname());
        assertEquals("newProfilePicture", result.getProfilePicture());
        assertEquals("testuser@example.com", result.getEmail());
        assertTrue(result.getIsAdultVerified());
        assertEquals("VIP", result.getLevel());
    }

}
