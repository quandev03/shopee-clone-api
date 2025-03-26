package com.example.banhangapi.api.repository;

import com.example.banhangapi.api.entity.Category;
import com.example.banhangapi.api.entity.ProductEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
//import javax.persistence.criteria.*;

@Slf4j
public class ProductSpecification {

    public static Specification<ProductEntity> searchProducts(
            Long minPrice,
            Long maxPrice,
            String categoryId,
            Integer rating,
            String nameProduct,
            String sort) {

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
                // Thêm dấu "%" ở đầu và cuối để tìm kiếm chuỗi con trong nameProduct
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("nameProduct"), "%" + nameProduct + "%"));
            }
            log.info(sort);
            assert query != null;
            return predicate;
        };
    }
}
