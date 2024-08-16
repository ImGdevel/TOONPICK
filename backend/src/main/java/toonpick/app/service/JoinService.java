package toonpick.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.dto.JoinRequestDTO;
import toonpick.app.entity.User;
import toonpick.app.repository.UserRepository;

@Service
public class JoinService {

    private final Logger LOGGER = LoggerFactory.getLogger(JoinService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public  JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    public void createUser(JoinRequestDTO joinRequestDTO) {
        if (userRepository.existsByUsername(joinRequestDTO.getUsername())) {
            throw new IllegalArgumentException("Genre with name " + joinRequestDTO.getUsername() + " already exists.");
        }

        User user = new User();
        user.update(
                joinRequestDTO.getUsername(),
                bCryptPasswordEncoder.encode(joinRequestDTO.getPassword()),
                "ROLE_USER"
        );
        user = userRepository.save(user);

    }
}
