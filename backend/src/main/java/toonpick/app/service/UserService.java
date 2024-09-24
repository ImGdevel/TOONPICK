package toonpick.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import toonpick.app.dto.UserDTO;
import toonpick.app.entity.User;
import toonpick.app.mapper.UserMapper;
import toonpick.app.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                                  .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return userMapper.userToUserDto(user);
    }
}
