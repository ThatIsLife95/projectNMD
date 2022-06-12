package com.example.demo.sercurity;

import com.example.demo.enums.EAuthProvider;
import com.example.demo.exceptions.BusinessException;
import com.example.demo.repository.AuthUserRepository;

import java.util.Map;


public class OAuth2UserInfoFactory {



    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(EAuthProvider.google.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(EAuthProvider.facebook.toString())) {
            return null;
//            return new FacebookOAuth2UserInfo(attributes);
        }  else {
            throw new BusinessException("","");
        }
    }
}
