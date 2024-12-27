package toonpick.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.dto.JoinRequestDTO;
import toonpick.app.entity.Member;
import toonpick.app.repository.MemberRepository;

import java.util.Random;

@Service
public class JoinService {

    private final Logger LOGGER = LoggerFactory.getLogger(JoinService.class);

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    public void createMember(JoinRequestDTO joinRequestDTO) {
        if (memberRepository.existsByUsername(joinRequestDTO.getUsername())) {
            String errorMessage = "Username " + joinRequestDTO.getUsername() + " already exists.";
            LOGGER.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        Member member = Member.builder()
                .username(joinRequestDTO.getUsername())
                .nickname(generateRandomNickname())
                .password(bCryptPasswordEncoder.encode(joinRequestDTO.getPassword()))
                .email(joinRequestDTO.getEmail())
                .role("ROLE_USER")
                .isAdultVerified(false)
                .profilePicture("default_profile_img.png")
                .build();

        memberRepository.save(member);

        LOGGER.info("Member {} created successfully.", joinRequestDTO.getUsername());
    }

    private String generateRandomNickname() {
        String[] words = {"Moon", "Star", "Sky", "Blue", "Red", "Cloud", "Ocean", "Fire", "Leaf", "Snow"};
        Random rand = new Random();
        String firstPart = words[rand.nextInt(words.length)];
        String secondPart = words[rand.nextInt(words.length)];
        return firstPart + secondPart;
    }

}
