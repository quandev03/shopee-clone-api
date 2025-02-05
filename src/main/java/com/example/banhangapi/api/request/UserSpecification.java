package com.example.banhangapi.api.request;

import com.example.banhangapi.api.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> hasUsername(String username) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("username"), username);
    }

    public static Specification<User> hasAgeGreaterThan(int age) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("age"), age);
    }
}
