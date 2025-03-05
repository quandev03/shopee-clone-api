package com.example.banhangapi.api.repository;

import com.example.banhangapi.api.dto.CartResponseDTO;
import com.example.banhangapi.api.entity.Cart;
import com.example.banhangapi.api.entity.Comment;
import com.example.banhangapi.api.entity.ProductEntity;
import com.example.banhangapi.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, String>, JpaSpecificationExecutor<Cart> {
    List<CartResponseDTO> findByCreatedBy(User createdBy);
    Boolean existsByProductAndCreatedBy(ProductEntity id, User createdBy);
    List<Cart> findAllByCheckedAndCreatedBy(Boolean check, User createBy);
}
