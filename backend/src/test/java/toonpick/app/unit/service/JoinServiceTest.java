package toonpick.app.unit.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import toonpick.app.dto.JoinRequestDTO;
import toonpick.app.entity.User;
import toonpick.app.repository.UserRepository;
import toonpick.app.service.JoinService;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JoinServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private JoinService joinService;

    public JoinServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("정상적으로 유저 생성")
    void testCreateUserSuccess() {
        JoinRequestDTO joinRequestDTO = new JoinRequestDTO("testUser", "test@example.com", "password");

        when(userRepository.existsByUsername("testUser")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        joinService.createUser(joinRequestDTO);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("중복된 사용자 이름으로 유저 생성 실패")
    void testCreateUserDuplicateUsername() {
        JoinRequestDTO joinRequestDTO = new JoinRequestDTO("testUser", "test@example.com", "password");

        when(userRepository.existsByUsername("testUser")).thenReturn(true);

        assertThatThrownBy(() -> joinService.createUser(joinRequestDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Username testUser already exists.");
    }
}
