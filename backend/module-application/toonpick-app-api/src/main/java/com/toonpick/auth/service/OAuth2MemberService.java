package com.toonpick.auth.service;


import com.toonpick.dto.OAuth2ResponseFactory;
import com.toonpick.auth.user.CustomOAuth2UserDetails;
import com.toonpick.dto.MemberDTO;
import com.toonpick.dto.OAuth2Response;
import com.toonpick.entity.Member;
import com.toonpick.enums.MemberRole;
import com.toonpick.member.mapper.MemberMapper;
import com.toonpick.repository.MemberRepository;
import com.toonpick.utils.ShortIdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class OAuth2MemberService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response auth2Response = OAuth2ResponseFactory.of(registrationId, oAuth2User.getAttributes());

        // 존재한다면 로그인, 없다면 회원가입
        Member member = memberRepository.findByUsername(auth2Response.getProviderId())
                .orElseGet(()->registerMember(auth2Response));

        MemberDTO memberDto = memberMapper.memberToMemberDTO(member);

        return new CustomOAuth2UserDetails(memberDto);
    }

    @Transactional
    public Member registerMember(OAuth2Response auth2Response){
        // Member 생성 기본 양식
        String provider = auth2Response.getProvider();
        String providerId = auth2Response.getProviderId();
        String username = provider + providerId;
        String nickname = generateNickname();
        String email = auth2Response.getEmail();

        Member member =  Member.builder()
                .username(username)
                .email(email)
                .nickname(nickname)
                .provider(provider)
                .providerId(providerId)
                .role(MemberRole.ROLE_USER)
                .build();

        member.verifyEmail();
        member.agreeTerms();
        member.activateAccount();

        return memberRepository.save(member);
    }

    private String generateNickname() {
        String nickname;
        do {
            nickname = "user-" + ShortIdUtil.generateRandomId();
        } while (memberRepository.existsByNickname(nickname));
        return nickname;
    }
}
