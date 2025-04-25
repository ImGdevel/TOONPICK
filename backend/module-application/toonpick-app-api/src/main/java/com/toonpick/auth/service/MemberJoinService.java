package com.toonpick.auth.service;

import com.toonpick.dto.JoinRequest;
import com.toonpick.entity.Member;
import com.toonpick.enums.MemberRole;
import com.toonpick.exception.UserAlreadyRegisteredException;
import com.toonpick.repository.MemberRepository;
import com.toonpick.type.ErrorCode;
import com.toonpick.utils.ShortIdUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberJoinService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberJoinService.class);

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Member registerMember(JoinRequest joinRequest) {
        String username = joinRequest.getEmail();
        String nickname = generateNickname();
        String password = bCryptPasswordEncoder.encode(joinRequest.getPassword());
        String email = joinRequest.getEmail();

        // 이미 존재하는 유저인지 검증
        if (memberRepository.existsByUsername(username)) {
            throw new UserAlreadyRegisteredException(ErrorCode.USER_ALREADY_REGISTERED, username);
        }

        // Member 생성 기본 양식
        Member member = Member.builder()
                .username(username)
                .nickname(nickname)
                .password(password)
                .email(email)
                .role(MemberRole.ROLE_USER)
                .build();

        member.verifyEmail();
        member.agreeTerms();
        member.activateAccount();

        Member savedMember = memberRepository.save(member);
        LOGGER.info("Member {} created successfully.", username);
        return savedMember;
    }

    private String generateNickname() {
        String nickname;
        do {
            nickname = "user-" + ShortIdUtil.generateRandomId();
        } while (memberRepository.existsByNickname(nickname));
        return nickname;
    }
}
