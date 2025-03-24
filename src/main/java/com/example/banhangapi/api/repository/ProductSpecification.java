package com.example.banhangapi.api.repository;

import com.example.banhangapi.api.entity.Category;
import com.example.banhangapi.api.entity.ProductEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
//import javax.persistence.criteria.*;

public class ProductSpecification {

    public static Specification<ProductEntity> searchProducts(
            Long minPrice,
            Long maxPrice,
            String categoryId,
            Integer rating,
            String nameProduct) {

        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            // Filter by minPrice and maxPrice
            if (minPrice != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            // Filter by categoryId
            if (categoryId != null && !categoryId.isEmpty()) {
                Join<ProductEntity, Category> categoryJoin = root.join("category", JoinType.INNER);
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(categoryJoin.get("id"), categoryId));
            }

            if (rating != null) { // Check if rating is not null
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("rating"), rating));
            }

            if (nameProduct != null && !nameProduct.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("nameProduct"), nameProduct));
            }

            // Add pagination
            query.orderBy(criteriaBuilder.asc(root.get("nameProduct"))); // Example ordering, you can change it.
            return predicate;
        };
    }
}
