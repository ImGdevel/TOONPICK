package toonpick.app.security.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import toonpick.app.dto.member.MemberResponseDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomOAuth2UserDetails implements OAuth2User {

    private final MemberResponseDTO memberResponseDTO;

    public CustomOAuth2UserDetails(MemberResponseDTO memberResponseDTO){
        this.memberResponseDTO = memberResponseDTO;
    }

    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("username", memberResponseDTO.getUsername());
        attributes.put("role", memberResponseDTO.getRole());
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
                           @Override
                           public String getAuthority() {
                               return memberResponseDTO.getRole();
                           }
                       }

        );

        return collection;
    }

    @Override
    public String getName() {
        return memberResponseDTO.getUsername();
    }


    public String getUsername() {
        return memberResponseDTO.getUsername();
    }
}
