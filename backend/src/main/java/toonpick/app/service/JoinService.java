package toonpick.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.dto.JoinRequestDTO;
import toonpick.app.entity.User;
import toonpick.app.repository.UserRepository;

import java.util.Random;

@Service
public class JoinService {

    private final Logger LOGGER = LoggerFactory.getLogger(JoinService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    public void createUser(JoinRequestDTO joinRequestDTO) {
        if (userRepository.existsByUsername(joinRequestDTO.getUsername())) {
            String errorMessage = "Username " + joinRequestDTO.getUsername() + " already exists.";
            LOGGER.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        User user = User.builder()
                .username(joinRequestDTO.getUsername())
                .nickname(generateRandomNickname())
                .password(bCryptPasswordEncoder.encode(joinRequestDTO.getPassword()))
                .email(joinRequestDTO.getEmail())
                .role("ROLE_USER")
                .isAdultVerified(false)
                .profilePicture("default_profile_img.png")
                .build();

        userRepository.save(user);

        LOGGER.info("User {} created successfully.", joinRequestDTO.getUsername());
    }

    private String generateRandomNickname() {
        String[] words = {"Moon", "Star", "Sky", "Blue", "Red", "Cloud", "Ocean", "Fire", "Leaf", "Snow"};
        Random rand = new Random();
        String firstPart = words[rand.nextInt(words.length)];
        String secondPart = words[rand.nextInt(words.length)];
        return firstPart + secondPart;
    }

}
