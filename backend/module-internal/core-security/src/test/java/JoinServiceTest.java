import com.toonpick.entity.Member;
import com.toonpick.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.toonpick.dto.JoinRequest;
import com.toonpick.service.JoinService;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("UnitTest")
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
    void testRegisterNewMemberSuccess() {
        JoinRequest joinRequest = new JoinRequest("testUser", "test@example.com", "password");

        when(memberRepository.existsByUsername("testUser")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        joinService.registerNewMember(joinRequest);

        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("중복된 사용자 이름으로 유저 생성 실패")
    void testRegisterNewMemberDuplicateUsername() {
        JoinRequest joinRequest = new JoinRequest("testUser", "test@example.com", "password");

        when(memberRepository.existsByUsername("testUser")).thenReturn(true);

        assertThatThrownBy(() -> joinService.registerNewMember(joinRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Username testUser already exists.");
    }
}
