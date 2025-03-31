package com.toonpick.service;

import com.toonpick.dto.MemberResponseDTO;
import com.toonpick.entity.Member;
import com.toonpick.mapper.MemberMapper;
import com.toonpick.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.toonpick.dto.MemberProfileDetailsResponseDTO;
import com.toonpick.dto.MemberProfileRequestDTO;
import com.toonpick.dto.MemberProfileResponseDTO;
import com.toonpick.type.ErrorCode;
import com.toonpick.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    // Member 프로필 조회
    @Transactional(readOnly = true)
    public MemberProfileResponseDTO getProfile(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));
        return memberMapper.memberToProfileResponseDTO(member);
    }

    // Member 상세 프로필 조회
    @Transactional(readOnly = true)
    public MemberProfileDetailsResponseDTO getProfileDetails(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));
        return memberMapper.memberToProfileDetailsResponseDTO(member);
    }

    // Member 프로필 업데이트
    @Transactional
    public void updateProfile(String username, MemberProfileRequestDTO memberProfileRequestDTO) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));

        member.updateProfile(
                memberProfileRequestDTO.getNickname()
        );

        memberRepository.save(member);
    }

    @Transactional
    public void updateProfileImage(String username, String profileImage){
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        member.updateProfileImage(profileImage);

        memberRepository.save(member);
    }

    // Member 패스워드 변경
    @Transactional
    public void changePassword(String username, String newPassword) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));

        member.changePassword(newPassword);

        memberRepository.save(member);
    }

    // Member 성인 인증
    @Transactional
    public void verifyAdult(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));

        member.verifyAdult();

        memberRepository.save(member);
    }

    // Member 조회
    @Transactional(readOnly = true)
    public MemberResponseDTO getMemberByUsername(String username) {
        Member member = memberRepository.findByUsername(username)
                                  .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));
        return memberMapper.memberToMemberResponseDTO(member);
    }

}
