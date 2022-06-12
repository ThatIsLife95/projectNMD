package com.example.demo.utils;

import com.example.demo.payload.request.FilterRequest;
import com.example.demo.entity.auth.AuthUser;
import com.example.demo.enums.ESearchOperation;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    public static Specification<AuthUser> getSpecifications(List<FilterRequest> filters) {
        if (filters.size() == 0) {
            return null;
        }
        Specification<AuthUser> conditions = Specification.where(getSpecification(filters.remove(0)));
        for (FilterRequest filterRequest : filters) {
            conditions = conditions.and(getSpecification(filterRequest));
        }
        return conditions;
    }

    public static Specification<AuthUser> getSpecification(FilterRequest filterRequest) {
        try {
            String key = filterRequest.getKey();
            String type = AuthUser.class.getDeclaredField(key).getGenericType().getTypeName();
            // Chưa constant
            if ("java.lang.String".equals(type)) {
                return createSpecificationWithStringType(key, filterRequest.getOperation(), filterRequest.getValue());
            } else if ("int".equals(type)) {
                return createSpecificationWithIntegerType(key, filterRequest.getOperation(), Integer.parseInt(filterRequest.getValue()));
            } else if ("java.time.LocalDateTime".equals(type)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDateTime dateTime = LocalDateTime.parse(filterRequest.getValue(), formatter);
                return createSpecificationWithLocalDateTimeType(key, filterRequest.getOperation(), LocalDateTime.parse(filterRequest.getValue(), formatter));
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

//    public static String getJWT(CustomUserDetails user, HttpServletRequest request, long expirationTime) {
//        Algorithm algorithm = Algorithm.HMAC256(JwtConstants.SECRET.getBytes());
//        return JWT.create()
//                .withSubject(user.getUsername())
//                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
//                .withIssuer(request.getRequestURL().toString())
//                .withClaim(JwtConstants.ROLES, user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
//                .sign(algorithm);
//    }
}
