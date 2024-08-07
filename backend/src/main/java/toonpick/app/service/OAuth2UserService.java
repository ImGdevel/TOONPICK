package toonpick.app.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import toonpick.app.dto.*;

@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        String registrationID = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = null;
        if(registrationID.equals("naver")){
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());

        } else if (registrationID.equals("Google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setUsername(oAuth2Response.getName());
        userDTO.setRole("ROLE_USER");

        return new CustomOAuth2User(userDTO);

    }
}
