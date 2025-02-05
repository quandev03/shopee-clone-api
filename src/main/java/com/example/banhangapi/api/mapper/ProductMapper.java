package com.example.banhangapi.api.mapper;

import com.example.banhangapi.api.entity.Product;
import com.example.banhangapi.api.entity.ProductDTO;
import com.example.banhangapi.api.request.RequestCreateProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
    Product toProduct(ProductDTO productDTO);
    ProductDTO toProductDTO(Product product);

    @Mapping(source = "price", target = "price")
    Product ReqToProduct(RequestCreateProduct requestCreateProduct);
}
