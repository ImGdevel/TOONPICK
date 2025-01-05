package toonpick.app.auth.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.auth.dto.JoinRequest;
import toonpick.app.member.entity.Member;
import toonpick.app.member.repository.MemberRepository;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final Logger LOGGER = LoggerFactory.getLogger(JoinService.class);

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Transactional
    public void createMember(JoinRequest joinRequest) {

        String username = joinRequest.getEmail();

        if (memberRepository.existsByUsername(username)) {
            String errorMessage = "Username " + username + " already exists.";
            LOGGER.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        Member member = Member.builder()
                .username(username)
                .nickname(generateRandomNickname())
                .password(bCryptPasswordEncoder.encode(joinRequest.getPassword()))
                .email(joinRequest.getEmail())
                .role("ROLE_USER")
                .isAdultVerified(false)
                .profilePicture("default_profile_img.png")
                .build();

        memberRepository.save(member);

        LOGGER.info("Member {} created successfully.", joinRequest.getUsername());
    }

    private String generateRandomNickname() {
        String[] words = {"Moon", "Star", "Sky", "Blue", "Red", "Cloud", "Ocean", "Fire", "Leaf", "Snow"};
        Random rand = new Random();
        String firstPart = words[rand.nextInt(words.length)];
        String secondPart = words[rand.nextInt(words.length)];
        return firstPart + secondPart;
    }

}
