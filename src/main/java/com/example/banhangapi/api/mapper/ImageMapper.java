package com.example.banhangapi.api.mapper;

import com.example.banhangapi.api.dto.ImageResponseDTO;
import com.example.banhangapi.api.entity.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    @Mapping(source = "description", target = "description")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "pathImage", target = "pathImage")
    ImageResponseDTO toImageResponseDTO(Image image);
}
