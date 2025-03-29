package toonpick.app.security.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.domain.member.MemberRole;
import toonpick.app.exception.ErrorCode;
import toonpick.app.security.dto.JoinRequest;
import toonpick.app.exception.exception.UserAlreadyRegisteredException;
import toonpick.app.domain.member.Member;
import toonpick.app.repository.MemberRepository;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class JoinService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JoinService.class);

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Member registerNewMember(JoinRequest joinRequest) {
        String username = joinRequest.getEmail();

        if (memberRepository.existsByUsername(username)) {
            throw new UserAlreadyRegisteredException(ErrorCode.USER_ALREADY_REGISTERED, username);
        }

        String nickname = generateUniqueNickname();

        Member member = Member.builder()
                .username(username)
                .nickname(nickname)
                .password(bCryptPasswordEncoder.encode(joinRequest.getPassword()))
                .email(joinRequest.getEmail())
                .role(MemberRole.ROLE_USER)
                .isAdultVerified(false)
                .profileImage(null)
                .build();

        Member savedMember = memberRepository.save(member);
        LOGGER.info("Member {} created successfully.", username);
        return savedMember;
    }

    private String generateUniqueNickname() {
        String[] words = {"Moon", "Star", "Sky", "Blue", "Red", "Cloud", "Ocean", "Fire", "Leaf", "Snow"};
        Random rand = new Random();
        String nickname;
        do {
            String firstPart = words[rand.nextInt(words.length)];
            String secondPart = words[rand.nextInt(words.length)];
            int randomNumber = rand.nextInt(9000) + 1000;
            nickname = firstPart + secondPart + randomNumber;
        } while (memberRepository.existsByNickname(nickname));
        return nickname;
    }

}
