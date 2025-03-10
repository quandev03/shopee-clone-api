package com.example.banhangapi.api.repository;

import com.example.banhangapi.api.entity.Category;
import com.example.banhangapi.api.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository  extends JpaRepository<ProductEntity, String>, JpaSpecificationExecutor<ProductEntity> {
    boolean existsByNameProduct(String name);
    Optional<ProductEntity> findById(String id);
    List<ProductEntity> findAllByCategory(Category category);
}
