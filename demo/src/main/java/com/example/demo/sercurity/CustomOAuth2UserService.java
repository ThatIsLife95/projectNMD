package com.example.demo.sercurity;

import com.example.demo.entity.auth.AuthUser;
import com.example.demo.enums.EAuthProvider;
import com.example.demo.exceptions.BusinessException;
import com.example.demo.repository.AuthUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;

//@Service
//@Transactional
//@AllArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

//    private final AuthUserRepository userRepository;
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2User oAuth2User = super.loadUser(userRequest);
//        Map<String, Object> attributes = oAuth2User.getAttributes();
//        String email = (String) attributes.get("email");
//        String provider = userRequest.getClientRegistration().getRegistrationId();
//        Optional<AuthUser> userOptional = userRepository.findByEmail(email);
//        if (userOptional.isPresent()) {
//            AuthUser user = userOptional.get();
//            if (EAuthProvider.valueOf(provider).equals(user.getProvider())) {
//                return new UserPrincipal(user.getUsername(), user.getAuthorities());
//            } else {
//                // TODO: Chưa tạo exception
//                throw new BusinessException("", "");
//            }
//        } else {
//            UserPrincipal user1 = new UserPrincipal(attributes);
//            user1.setName("Dung");
//            user1.setUsername("nmd");
////            return new UserPrincipal(attributes);
//            return user1;
//        }
//    }
}
