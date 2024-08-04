package toonpick.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.controller.JoinController;
import toonpick.app.dto.GenreDTO;
import toonpick.app.dto.JoinDTO;
import toonpick.app.dto.UserDTO;
import toonpick.app.entity.Genre;
import toonpick.app.entity.User;
import toonpick.app.exception.ResourceNotFoundException;
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
    public void createUser(JoinDTO joinDTO) {
        if (userRepository.existsByUsername(joinDTO.getUsername())) {
            throw new IllegalArgumentException("Genre with name " + joinDTO.getUsername() + " already exists.");
        }

        User user = new User();
        user.update(
                joinDTO.getUsername(),
                bCryptPasswordEncoder.encode(joinDTO.getPassword()),
                "ROLE_ADMIN"
        );
        user = userRepository.save(user);

    }
}
