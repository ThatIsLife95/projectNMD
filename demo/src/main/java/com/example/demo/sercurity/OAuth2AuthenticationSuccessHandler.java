package com.example.demo.sercurity;

import com.example.demo.entity.auth.AuthUser;
import com.example.demo.enums.EAuthProvider;
import com.example.demo.exceptions.BusinessException;
import com.example.demo.repository.AuthUserRepository;
import com.example.demo.service.AuthTokenService;
import com.example.demo.utils.Common;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

        private final AuthUserRepository userRepository;

    private final AuthTokenService tokenService;

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        Optional<AuthUser> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            HttpSession session = request.getSession();
            String displayName = (String) attributes.get("name");
            session.setAttribute("displayName", displayName);
            session.setAttribute("email", email);
            String token = Common.getToken();
            tokenService.saveToken(email, token);
            String targetUrl = "http://localhost:8080/tao-tai-khoan" + "?token=" + token;
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } else {
            // TODO: trả về jwt token
//            Map<String, String> tokens = jwtTokenProvider.getTokens(userPrincipal, request);

        }

    }
}
