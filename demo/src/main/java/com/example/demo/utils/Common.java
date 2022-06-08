package com.example.demo.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.constants.JwtConstants;
import com.example.demo.dto.CustomUserDetails;
import com.example.demo.dto.FilterDto;
import com.example.demo.entity.auth.AuthUser;
import com.example.demo.enums.ESearchOperation;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Common {
    public static String getToken() {
        return UUID.randomUUID().toString();
    }

    public static Map<String, Object> getTemplateModel(String link, String greeting, String text, String button) {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("link", link);
        templateModel.put("greeting", greeting);
        templateModel.put("text", text);
        templateModel.put("button", button);
        return  templateModel;
    }



    public static Specification<AuthUser> getSpecifications(List<FilterDto> filters) {
        if (filters.size() == 0) {
            return null;
        }
        Specification<AuthUser> conditions = Specification.where(getSpecification(filters.remove(0)));
        for (FilterDto filterDto : filters) {
            conditions = conditions.and(getSpecification(filterDto));
        }
        return conditions;
    }

    public static Specification<AuthUser> getSpecification(FilterDto filterDto) {
        try {
            String key = filterDto.getKey();
            String type = AuthUser.class.getDeclaredField(key).getGenericType().getTypeName();
            // Chưa constant
            if ("java.lang.String".equals(type)) {
                return createSpecificationWithStringType(key, filterDto.getOperation(), filterDto.getValue());
            } else if ("int".equals(type)) {
                return createSpecificationWithIntegerType(key, filterDto.getOperation(), Integer.parseInt(filterDto.getValue()));
            } else if ("java.time.LocalDateTime".equals(type)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDateTime dateTime = LocalDateTime.parse(filterDto.getValue(), formatter);
                return createSpecificationWithLocalDateTimeType(key, filterDto.getOperation(), LocalDateTime.parse(filterDto.getValue(), formatter));
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Specification<AuthUser> createSpecificationWithStringType(String key, ESearchOperation operation, String value) {
        switch (operation) {
            case EQUAL:
                return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(key), value);
            case STARTS_WITH:
                return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(key), value + "%");
            case ENDS_WITH:
                return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(key), "%" + value);
            case CONTAINS:
                return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(key), "%" + value + "%");
            default:
                return null;
        }
    }

    private static Specification<AuthUser> createSpecificationWithLocalDateTimeType(String key, ESearchOperation operation, LocalDateTime value) {
        switch (operation) {
            case EQUAL:
                return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(key), value);
            case GREATER_THAN:
                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(key), value);
            case LESS_THAN:
                return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get(key), value);
            case GREATER_THAN_OR_EQUAL_TO:
                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(key), value);
            case LESS_THAN_OR_EQUAL_TO:
                return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(key), value);
            default:
                return null;
        }
    }

    // Đang bị lặp code
    private static Specification<AuthUser> createSpecificationWithIntegerType(String key, ESearchOperation operation, int value) {
        switch (operation) {
            case EQUAL:
                return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(key), value);
            case GREATER_THAN:
                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(key), value);
            case LESS_THAN:
                return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get(key), value);
            case GREATER_THAN_OR_EQUAL_TO:
                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(key), value);
            case LESS_THAN_OR_EQUAL_TO:
                return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(key), value);
            default:
                return null;
        }
    }

    public static String getJWT(CustomUserDetails user, HttpServletRequest request, long expirationTime) {
        Algorithm algorithm = Algorithm.HMAC256(JwtConstants.SECRET.getBytes());
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .withIssuer(request.getRequestURL().toString())
                .withClaim(JwtConstants.ROLES, user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }
}
