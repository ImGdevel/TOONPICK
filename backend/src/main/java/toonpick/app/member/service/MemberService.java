package toonpick.app.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.member.dto.MemberDTO;
import toonpick.app.member.entity.Member;
import toonpick.app.member.mapper.MemberMapper;
import toonpick.app.member.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Autowired
    public MemberService(MemberRepository memberRepository, MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
    }

    @Transactional(readOnly = true)
    public MemberDTO getUserById(Long id) {
        Member member = memberRepository.findById(id)
                                  .orElseThrow(() -> new UsernameNotFoundException("Member not found"));
        return memberMapper.userToUserDto(member);
    }

    @Transactional(readOnly = true)
    public Long getUserIdByUsername(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Member not found with username: " + username));
        return member.getId();
    }
}
