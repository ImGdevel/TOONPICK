package toonpick.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import toonpick.app.dto.*;
import toonpick.app.entity.User;
import toonpick.app.mapper.UserMapper;
import toonpick.app.repository.UserRepository;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationID = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = createOAuth2Response(registrationID, oAuth2User.getAttributes());
        if (oAuth2Response == null) {
            throw new OAuth2AuthenticationException("Unsupported provider: " + registrationID);
        }

        User user = getOrCreateUser(oAuth2Response);
        UserDTO userDTO = userMapper.userToUserDto(user);

        return new CustomOAuth2User(userDTO);
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

    // DB에 User 등록
    private User getOrCreateUser(OAuth2Response oAuth2Response) {
        String username = generateUsername(oAuth2Response);

        return userRepository.findByUsername(username).orElseGet(() -> {
            User newUser = createUser(oAuth2Response, username);
            return userRepository.save(newUser);
        });
    }

    // UserName 생성
    private String generateUsername(OAuth2Response oAuth2Response) {
        return oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
    }

    // User 등록
    private User createUser(OAuth2Response oAuth2Response, String username) {
        return User.builder()
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
