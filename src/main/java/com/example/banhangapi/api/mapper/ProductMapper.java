package com.example.banhangapi.api.mapper;

import com.example.banhangapi.api.dto.ProductDTO;
import com.example.banhangapi.api.entity.ProductEntity;
import com.example.banhangapi.api.request.RequestCreateProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {ImageMapper.class})
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
    ProductEntity toProduct(ProductDTO productDTO);
    ProductDTO toProductDTO(ProductEntity product);

    @Mapping(source = "price", target = "price")
    ProductEntity ReqToProduct(RequestCreateProduct requestCreateProduct);
}
