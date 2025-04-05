package com.toonpick.service;

import com.toonpick.dto.MemberDTO;
import com.toonpick.dto.oauth2responseImpl.GoogleResponse;
import com.toonpick.dto.oauth2responseImpl.NaverResponse;
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
import com.toonpick.user.CustomOAuth2UserDetails;
import com.toonpick.dto.OAuth2Response;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationID = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = createOAuth2Response(registrationID, oAuth2User.getAttributes());
        if (oAuth2Response == null) {
            throw new OAuth2AuthenticationException("Unsupported provider: " + registrationID);
        }

        Member member = getOrCreateUser(oAuth2Response);
        MemberDTO memberResponseDTO = memberMapper.memberToMemberDTO(member);

        return new CustomOAuth2UserDetails(memberResponseDTO);
    }

    // 서비스 제공자에 따른 OAuth2Response 생성
    private OAuth2Response createOAuth2Response(String registrationID, Map<String, Object> attributes) {
        switch (registrationID.toLowerCase()) {
            case "naver":
                return new NaverResponse(attributes);
            case "google":
                return new GoogleResponse(attributes);
            default:
                return null;
        }
    }

    // DB에 Member 등록
    private Member getOrCreateUser(OAuth2Response oAuth2Response) {
        String username = generateUsername(oAuth2Response);

        return memberRepository.findByUsername(username).orElseGet(() -> {
            Member newMember = createUser(oAuth2Response, username);
            return memberRepository.save(newMember);
        });
    }

    // UserName 생성
    private String generateUsername(OAuth2Response oAuth2Response) {
        return oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
    }

    // Member 등록
    private Member createUser(OAuth2Response oAuth2Response, String username) {
        return Member.builder()
                .username(username)
                .password("")
                .email(oAuth2Response.getEmail())
                .nickname(oAuth2Response.getName())
                .profileImage("")
                .isAdultVerified(false)
                .role(MemberRole.ROLE_USER)
                .build();
    }

}
