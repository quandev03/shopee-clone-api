package com.example.banhangapi.api.repository;

import com.example.banhangapi.api.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryRepository extends JpaRepository<Category, String>, JpaSpecificationExecutor<Category> {
}
