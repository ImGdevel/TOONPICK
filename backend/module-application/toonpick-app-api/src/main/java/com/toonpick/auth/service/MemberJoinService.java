package com.toonpick.auth.service;

import com.toonpick.dto.JoinRequest;
import com.toonpick.domain.member.entity.Member;
import com.toonpick.domain.member.enums.MemberRole;
import com.toonpick.exception.DuplicateResourceException;
import com.toonpick.domain.member.repository.MemberRepository;
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

        // 이미 존재하는 Member인지 검증
        String username = joinRequest.getEmail();
        if (memberRepository.existsByUsername(username)) {
            throw new DuplicateResourceException(ErrorCode.USER_ALREADY_REGISTERED, username);
        }

        // Member 등록
        Member newMember = createNewMember(joinRequest);
        Member savedMember = memberRepository.save(newMember);

        return savedMember;
    }

    /**
     * 새로운 Member 생성
     */
    private Member createNewMember(JoinRequest joinRequest){
        String username = joinRequest.getEmail();
        String nickname = generateNickname();
        String password = bCryptPasswordEncoder.encode(joinRequest.getPassword());
        String email = joinRequest.getEmail();

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

        return member;
    }

    /**
     * 무작위 Nickname 생성
     */
    private String generateNickname() {
        String nickname;
        do {
            nickname = "user-" + ShortIdUtil.generateRandomId();
        } while (memberRepository.existsByNickname(nickname));
        return nickname;
    }
}
