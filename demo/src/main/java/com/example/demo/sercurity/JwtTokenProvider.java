package com.example.demo.sercurity;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.constants.JwtConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {
    @Value("${app.auth.jwtSecret}")
    private String jwtSecret;

    @Value("${app.auth.jwtAccessExpirationInMs}")
    private int jwtAccessExpirationInMs;

    @Value("${app.auth.jwtRefreshExpirationInMs}")
    private int jwtRefreshExpirationInMs;


    public String generateAccessToken(CustomUserDetails user, HttpServletRequest request) {
        return generateToken(user, request, jwtAccessExpirationInMs);
    }

    public String generateRefreshToken(CustomUserDetails user, HttpServletRequest request) {
        return generateToken(user, request, jwtRefreshExpirationInMs);
    }

    public String generateToken(CustomUserDetails user, HttpServletRequest request, int jwtExpirationInMs) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .withIssuer(request.getRequestURL().toString())
                .withClaim(JwtConstants.ROLES, user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

//    public Long getUserIdFromJWT(String token) {
//        Claims claims = Jwts.parser()
//                .setSigningKey(jwtSecret)
//                .parseClaimsJws(token)
//                .getBody();
//        return Long.parseLong(claims.getSubject());
//    }

//    public boolean validateToken(String authToken) {
//        try {
//            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
//            return true;
//        } catch (SignatureException ex) {
//            log.error("Invalid JWT signature");
//        } catch (MalformedJwtException ex) {
//            log.error("Invalid JWT token");
//        } catch (ExpiredJwtException ex) {
//            log.error("Expired JWT token");
//        } catch (UnsupportedJwtException ex) {
//            log.error("Unsupported JWT token");
//        } catch (IllegalArgumentException ex) {
//            log.error("JWT claims string is empty.");
//        }
//        return false;
//    }
}
