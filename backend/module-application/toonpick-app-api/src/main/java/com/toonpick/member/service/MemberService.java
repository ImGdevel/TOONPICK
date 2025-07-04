package com.toonpick.member.service;



import com.toonpick.domain.member.entity.Member;
import com.toonpick.common.exception.EntityNotFoundException;
import com.toonpick.member.mapper.MemberMapper;
import com.toonpick.member.request.MemberProfileRequestDTO;
import com.toonpick.member.response.MemberProfileDetailsResponse;
import com.toonpick.member.response.MemberProfileResponse;
import com.toonpick.member.response.MemberResponseDTO;
import com.toonpick.domain.member.repository.MemberRepository;
import com.toonpick.internal.storage.service.AwsS3StorageService;
import com.toonpick.common.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final AwsS3StorageService awsS3StorageService;

    // Member 프로필 조회
    @Transactional(readOnly = true)
    public MemberProfileResponse getProfile(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));
        return memberMapper.memberToProfileResponseDTO(member);
    }

    // Member 상세 프로필 조회
    @Transactional(readOnly = true)
    public MemberProfileDetailsResponse getProfileDetails(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));
        return memberMapper.memberToProfileDetailsResponseDTO(member);
    }

    // Member 프로필 업데이트
    @Transactional
    public void updateProfile(String username, MemberProfileRequestDTO memberProfileRequestDTO) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));

        member.updateNickname(
                memberProfileRequestDTO.getNickname()
        );

        memberRepository.save(member);
    }

    /**
     * Member 페스워드 변경
     */
    @Transactional
    public void changePassword(String username, String newPassword) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));

        member.changePassword(newPassword);

        memberRepository.save(member);
    }

    /**
     * 프로필 이미지 업데이트 - 스토리지에 업로드
     */
    public String updateProfileImage(String username, MultipartFile imageFile) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));

        String fileName = "profile-images/" + username + "/" + UUID.randomUUID() + ".webp";
        String profileImageUrl = awsS3StorageService.uploadFile(imageFile, fileName);

        member.updateProfileImage(profileImageUrl);
        memberRepository.save(member);

        return profileImageUrl;
    }


    // Member 성인 인증
    @Transactional
    public void verifyAdult(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));

        member.verifyAdult();

        memberRepository.save(member);
    }

    // Member 조회
    @Transactional(readOnly = true)
    public MemberResponseDTO getMemberByUsername(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));
        return memberMapper.memberToMemberResponseDTO(member);
    }

}
