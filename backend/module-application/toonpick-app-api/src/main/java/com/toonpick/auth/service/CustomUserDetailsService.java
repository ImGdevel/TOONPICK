package com.toonpick.auth.service;

import com.toonpick.entity.Member;
import com.toonpick.repository.MemberRepository;
import com.toonpick.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.toonpick.user.CustomUserDetails;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorCode.MEMBER_NOT_FOUND.getMessage() + username));
        return CustomUserDetails.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .role(String.valueOf(member.getRole()))
                .build();
    }
}
