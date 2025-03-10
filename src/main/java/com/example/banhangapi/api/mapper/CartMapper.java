package com.example.banhangapi.api.mapper;

import com.example.banhangapi.api.dto.CartResponseDTO;
import com.example.banhangapi.api.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ProductMapper.class})
public interface CartMapper {
    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    // Chuyển đổi từ Cart sang CartResponseDTO
    @Mapping(source = "product", target = "product")
    @Mapping(source = "quantityBuy", target = "quantity")
    CartResponseDTO toCartResponseDTO(Cart cart);
}