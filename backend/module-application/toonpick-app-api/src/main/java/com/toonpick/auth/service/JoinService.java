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

        // 이미 존재하는 유저인지 검증
        if (memberRepository.existsByUsername(username)) {
            throw new UserAlreadyRegisteredException(ErrorCode.USER_ALREADY_REGISTERED, username);
        }

        // 랜덤 nickname 생성
        String nickname;
        do {
            nickname = "user-" + ShortIdUtil.generateRandomId();
        }while (!memberRepository.existsByNickname(username));

        Member member = Member.builder()
                .username(username)
                .nickname(nickname)
                .password(bCryptPasswordEncoder.encode(joinRequest.getPassword()))
                .email(joinRequest.getEmail())
                .role(MemberRole.ROLE_USER)
                .profileImage(null)
                .build();

        Member savedMember = memberRepository.save(member);
        LOGGER.info("Member {} created successfully.", username);
        return savedMember;
    }

}
