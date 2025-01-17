package toonpick.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.dto.member.MemberProfileDetailsResponseDTO;
import toonpick.app.dto.member.MemberProfileRequestDTO;
import toonpick.app.dto.member.MemberProfileResponseDTO;
import toonpick.app.dto.member.MemberResponseDTO;
import toonpick.app.domain.member.Member;
import toonpick.app.mapper.MemberMapper;
import toonpick.app.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    // Member 프로필 조회
    @Transactional(readOnly = true)
    public MemberProfileResponseDTO getProfile(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found"));
        return memberMapper.memberToProfileResponseDTO(member);
    }

    // Member 상세 프로필 조회
    @Transactional(readOnly = true)
    public MemberProfileDetailsResponseDTO getProfileDetails(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found"));
        return memberMapper.memberToProfileDetailsResponseDTO(member);
    }

    // Member 프로필 업데이트
    @Transactional
    public void updateProfile(String username, MemberProfileRequestDTO memberProfileRequestDTO) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found"));

        member.updateProfile(
                memberProfileRequestDTO.getNickname(),
                memberProfileRequestDTO.getProfilePicture()
        );

        memberRepository.save(member);
    }

    // Member 패스워드 변경
    @Transactional
    public void changePassword(String username, String newPassword) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found"));

        member.changePassword(newPassword);

        memberRepository.save(member);
    }

    // Member 성인 인증
    @Transactional
    public void verifyAdult(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found"));

        member.verifyAdult();

        memberRepository.save(member);
    }

    // Member 조회
    @Transactional(readOnly = true)
    public MemberResponseDTO getMemberByUsername(String username) {
        Member member = memberRepository.findByUsername(username)
                                  .orElseThrow(() -> new UsernameNotFoundException("Member not found"));
        return memberMapper.memberToMemberResponseDTO(member);
    }

    // Member Id 조회
    @Transactional(readOnly = true)
    public Long getMemberIdByUsername(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Member not found with username: " + username));
        return member.getId();
    }

}
