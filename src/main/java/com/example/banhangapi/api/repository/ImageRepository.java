package com.example.banhangapi.api.repository;

import com.example.banhangapi.api.entity.Image;
import com.example.banhangapi.api.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.Optional;
@EnableJpaRepositories
public interface ImageRepository extends JpaRepository<Image, String>, JpaSpecificationExecutor<Image> {
    Optional<Image> findByProductAndDefaultImage(ProductEntity product, boolean defaultImage);
    List<Image> findAllByProduct(ProductEntity product);
}
