package toonpick.app.security.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import toonpick.app.dto.MemberDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomOAuth2UserDetails implements OAuth2User {

    private final MemberDTO memberDTO;

    public CustomOAuth2UserDetails(MemberDTO memberDTO){
        this.memberDTO = memberDTO;
    }

    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("username", memberDTO.getUsername());
        attributes.put("role", memberDTO.getRole());
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
                           @Override
                           public String getAuthority() {
                               return memberDTO.getRole();
                           }
                       }

        );

        return collection;
    }

    @Override
    public String getName() {
        return memberDTO.getUsername();
    }


    public String getUsername() {
        return memberDTO.getUsername();
    }
}
