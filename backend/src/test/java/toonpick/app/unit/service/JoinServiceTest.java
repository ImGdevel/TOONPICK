package toonpick.app.unit.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import toonpick.app.auth.dto.JoinRequest;
import toonpick.app.member.entity.Member;
import toonpick.app.member.repository.MemberRepository;
import toonpick.app.auth.service.JoinService;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JoinServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private JoinService joinService;

    public JoinServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("정상적으로 유저 생성")
    void testCreateMemberSuccess() {
        JoinRequest joinRequest = new JoinRequest("testUser", "test@example.com", "password");

        when(memberRepository.existsByUsername("testUser")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        joinService.createMember(joinRequest);

        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("중복된 사용자 이름으로 유저 생성 실패")
    void testCreateMemberDuplicateUsername() {
        JoinRequest joinRequest = new JoinRequest("testUser", "test@example.com", "password");

        when(memberRepository.existsByUsername("testUser")).thenReturn(true);

        assertThatThrownBy(() -> joinService.createMember(joinRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Username testUser already exists.");
    }
}
