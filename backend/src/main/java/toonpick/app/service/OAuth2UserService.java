package toonpick.app.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import toonpick.app.dto.*;
import toonpick.app.entity.User;
import toonpick.app.repository.UserRepository;

import java.util.Optional;

@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public OAuth2UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        String registrationID = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = null;
        if (registrationID.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());

        } else if (registrationID.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        // 리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();

        Optional<User> existDataOptional = userRepository.findByUsername(username);

        if (existDataOptional.isEmpty()) {
            User user = User.builder()
                    .username(username)
                    .password("")
                    .email(oAuth2Response.getEmail())
                    .nickname(oAuth2Response.getName())
                    .profilePicture("default_profile_img.png")
                    .isAdultVerified(false)
                    .role("ROLE_USER")
                    .build();

            userRepository.save(user);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setRole("ROLE_USER");

            return new CustomOAuth2User(userDTO);
        } else {
            User existData = existDataOptional.get();
            userRepository.save(existData);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(existData.getUsername());
            userDTO.setRole(existData.getRole());

            return new CustomOAuth2User(userDTO);
        }
    }
}
