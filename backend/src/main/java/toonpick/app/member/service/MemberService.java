package toonpick.app.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.member.dto.MemberDTO;
import toonpick.app.member.dto.MemberProfileDTO;
import toonpick.app.member.entity.Member;
import toonpick.app.member.mapper.MemberMapper;
import toonpick.app.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    // Member 프로필 조회
    @Transactional(readOnly = true)
    public MemberProfileDTO getProfile(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found"));
        return memberMapper.memberToProfileDto(member);
    }

    // Member 프로필 업데이트
    @Transactional
    public void updateProfile(String username, String nickname, String profilePicture) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found"));

        member.updateProfile(nickname, profilePicture);

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
    public MemberDTO getMemberByUsername(String username) {
        Member member = memberRepository.findByUsername(username)
                                  .orElseThrow(() -> new UsernameNotFoundException("Member not found"));
        return memberMapper.memberToMemberDto(member);
    }

    // Member Id 조회
    @Transactional(readOnly = true)
    public Long getMemberIdByUsername(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Member not found with username: " + username));
        return member.getId();
    }

}
