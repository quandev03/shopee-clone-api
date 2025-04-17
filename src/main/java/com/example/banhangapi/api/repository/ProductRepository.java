package com.example.banhangapi.api.repository;

import com.example.banhangapi.api.entity.Category;
import com.example.banhangapi.api.entity.ProductEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository  extends JpaRepository<ProductEntity, String>, JpaSpecificationExecutor<ProductEntity> {
    boolean existsByNameProduct(String name);
    Optional<ProductEntity> findById(String id);
    List<ProductEntity> findAllByCategory(Category category);
    // Tìm kiếm sản phẩm với các tham số
    List<ProductEntity> findAll(Specification<ProductEntity> spec);

    @Modifying
    @Transactional
    @Query("""
    UPDATE ProductEntity p
    SET p.viewedQuantity = p.viewedQuantity + 1
    WHERE p.id = :id
""")
    void updateView(@Param("id") String id);

    @Query("""
        SELECT COUNT(*) FROM ProductEntity p
    """)
    int countProduct();


    @Query("SELECT p FROM ProductEntity p WHERE p.quantity < 5")
    List<ProductEntity> productOutOfStockLong();

    @Query(value = """
        SELECT p FROM ProductEntity p
        WHERE (:minPrice IS NULL OR p.price >= :minPrice)
          AND (:maxPrice IS NULL OR p.price <= :maxPrice)
          AND (:name IS NULL OR LOWER(p.nameProduct) LIKE LOWER(CONCAT('%', :name, '%')))
          AND (:rating IS NULL OR p.rating >= :rating)
          AND (:categoryId IS NULL OR p.category.id = :categoryId)
    """, countQuery = """
        SELECT COUNT(p) FROM ProductEntity p
        WHERE (:minPrice IS NULL OR p.price >= :minPrice)
          AND (:maxPrice IS NULL OR p.price <= :maxPrice)
          AND (:name IS NULL OR LOWER(p.nameProduct) LIKE LOWER(CONCAT('%', :name, '%')))
          AND (:rating IS NULL OR p.rating >= :rating)
          AND (:categoryId IS NULL OR p.category.id = :categoryId)
    """)
    Page<ProductEntity> getAllProductByCondition(
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("name") String name,
            @Param("rating") Integer rating,
            @Param("categoryId") String categoryId,
            Pageable pageable
    );

    @Query(value = """
        UPDATE products p
        JOIN cart c ON p.id = c.product_id
        SET  p.quantity = p.quantity - c.quantity_buy,
            p.sold_quantity = p.sold_quantity + c.quantity_buy
        WHERE c.id = :cartId
    """, nativeQuery = true)
    void updateQuantityBuy(@Param("cartId") String cartId);

    @Modifying
    @Transactional
    @Query(value = """
        update products p set p.rating = :rating
        where p.id = :id
    """, nativeQuery = true)
    void updateRating(@Param("id") String id, @Param("rating") Integer rating);

}
