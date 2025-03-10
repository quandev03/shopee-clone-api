package com.example.banhangapi.api.mapper;

import com.example.banhangapi.api.dto.CategoryResponseDTO;
import com.example.banhangapi.api.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponseDTO categoryToCategoryResponseDTO(Category category);
}
