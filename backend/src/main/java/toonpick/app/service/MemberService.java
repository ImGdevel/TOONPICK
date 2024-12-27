package toonpick.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import toonpick.app.dto.MemberDTO;
import toonpick.app.entity.Member;
import toonpick.app.mapper.MemberMapper;
import toonpick.app.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Autowired
    public MemberService(MemberRepository memberRepository, MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
    }

    public MemberDTO getUserById(Long id) {
        Member member = memberRepository.findById(id)
                                  .orElseThrow(() -> new UsernameNotFoundException("Member not found"));
        return memberMapper.userToUserDto(member);
    }
}
