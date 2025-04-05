package com.toonpick.auth.service;


import com.toonpick.dto.OAuth2ResponseFactory;
import com.toonpick.auth.CustomOAuth2UserDetails;
import com.toonpick.dto.MemberDTO;
import com.toonpick.dto.OAuth2Response;
import com.toonpick.entity.Member;
import com.toonpick.enums.MemberRole;
import com.toonpick.mapper.MemberMapper;
import com.toonpick.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = OAuth2ResponseFactory.of(registrationId, oAuth2User.getAttributes());

        String uid = generateUsername(oAuth2Response);

        Member member = memberRepository.findByUsername(uid).orElseGet(() -> {
            Member newMember = createUser(oAuth2Response);
            return memberRepository.save(newMember);
        });

        MemberDTO memberDto = memberMapper.memberToMemberDTO(member);

        return new CustomOAuth2UserDetails(memberDto);
    }

    // UserName 생성
    private String generateUsername(OAuth2Response oAuth2Response) {
        return oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
    }
    // Member 등록
    private Member createUser(OAuth2Response oAuth2Response) {
        return Member.builder()
                .username(generateUsername(oAuth2Response))
                .password("")
                .email(oAuth2Response.getEmail())
                .nickname(oAuth2Response.getName())
                .profileImage("")
                .isAdultVerified(false)
                .role(MemberRole.ROLE_USER)
                .build();
    }
}
