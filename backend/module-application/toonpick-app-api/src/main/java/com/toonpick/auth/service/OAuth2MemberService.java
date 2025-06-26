package com.toonpick.auth.service;


import com.toonpick.dto.OAuth2ResponseFactory;
import com.toonpick.auth.user.CustomOAuth2UserDetails;
import com.toonpick.domain.member.dto.MemberDTO;
import com.toonpick.dto.OAuth2Response;
import com.toonpick.domain.member.entity.Member;
import com.toonpick.domain.member.entity.MemberSocialLogin;
import com.toonpick.domain.member.enums.MemberRole;
import com.toonpick.domain.member.enums.Provider;
import com.toonpick.member.mapper.MemberMapper;
import com.toonpick.domain.member.repository.MemberRepository;
import com.toonpick.domain.member.repository.MemberSocialLoginRepository;
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
    private final MemberSocialLoginRepository socialLoginRepository;

    // todo : 발견된 에러 -> 달못된 경로로 username이 달라져 OAuth2로그인이 실패한 경우 기본 로그인 페이지로 이동됨(email 유니크 제약으로 인한 중간 실패)

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response auth2Response = OAuth2ResponseFactory.of(registrationId, oAuth2User.getAttributes());

        // 존재한다면 로그인, 없다면 회원가입
        // todo : 만약 같은 사람으로 판별되는 경우 로그인을 통합해야한다.
        // todo : 해당 문제는 추후 고려한다. (현재는 소셜 로그인마다 별도의 계정이 생성된다고 가정한다)
        // todo : 다른 소셜 로그인의 경우 어떻게 같은 사람인지 반별할 수 있는가?
        MemberSocialLogin loginInfo = socialLoginRepository.findByProviderAndProviderId(
                Provider.fromString(auth2Response.getProvider()), auth2Response.getProviderId())
                .orElseGet(()->registerMember(auth2Response));

        Member member = loginInfo.getMember();

        MemberDTO memberDto = memberMapper.memberToMemberDTO(member);

        return new CustomOAuth2UserDetails(memberDto);
    }

    @Transactional
    public MemberSocialLogin registerMember(OAuth2Response auth2Response){
        // Member 생성 기본 양식
        String provider = auth2Response.getProvider();
        String providerId = auth2Response.getProviderId();
        String username = provider + providerId;
        String nickname = generateNickname();
        String email = auth2Response.getEmail();

        // 회원 등록
        Member member =  Member.builder()
                .username(username)
                .email(email)
                .nickname(nickname)
                .role(MemberRole.ROLE_USER)
                .build();

        // 소셜 로그인 등록
        MemberSocialLogin memberSocialLogin = MemberSocialLogin.builder()
                .member(member)
                .provider(Provider.fromString(provider))
                .providerId(providerId)
                .providerEmail(email)
                .build();

        member.verifyEmail();
        member.agreeTerms();
        member.activateAccount();

        memberRepository.save(member);

        return socialLoginRepository.save(memberSocialLogin);
    }

    private String generateNickname() {
        String nickname;
        do {
            nickname = "user-" + ShortIdUtil.generateRandomId();
        } while (memberRepository.existsByNickname(nickname));
        return nickname;
    }
}
