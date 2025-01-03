package toonpick.app.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import toonpick.app.auth.user.CustomOAuth2UserDetails;
import toonpick.app.auth.dto.oauth2responseImpl.GoogleResponse;
import toonpick.app.auth.dto.oauth2responseImpl.NaverResponse;
import toonpick.app.auth.dto.OAuth2Response;
import toonpick.app.member.dto.MemberDTO;
import toonpick.app.member.entity.Member;
import toonpick.app.member.mapper.MemberMapper;
import toonpick.app.member.repository.MemberRepository;

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
        MemberDTO memberDTO = memberMapper.memberToMemberDto(member);

        return new CustomOAuth2UserDetails(memberDTO);
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
                .profilePicture("default_profile_img.png")
                .isAdultVerified(false)
                .role("ROLE_USER")
                .build();
    }

}
