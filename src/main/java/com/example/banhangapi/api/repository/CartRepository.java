package com.example.banhangapi.api.repository;

import com.example.banhangapi.api.entity.Cart;
import com.example.banhangapi.api.entity.ProductEntity;
import com.example.banhangapi.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, String>, JpaSpecificationExecutor<Cart> {
    List<Cart> findByCreatedBy(User createdBy);
    Boolean existsByProductAndCreatedBy(ProductEntity id, User createdBy);
    Optional<Cart> findByProductAndCreatedBy(ProductEntity product, User createdBy);

    @Modifying
    @Transactional
    @Query("UPDATE Cart c SET c.quantityBuy = :quantityBuy WHERE c.id = :cartId")
    int updateCart(@Param("cartId") String cartId, @Param("quantityBuy") Integer quantity);

    @Modifying
    @Transactional
    @Query("DELETE FROM Cart c WHERE c.id = :cartId")
    int deleteCart(@Param("cartId") String cartId);
}
