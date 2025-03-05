package com.example.banhangapi.api.mapper;

import com.example.banhangapi.api.dto.OrderDetailDTO;
import com.example.banhangapi.api.entity.OrderDetails;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductMapper.class, UserMapper.class})
public interface OrderMapper  {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);
    OrderDetails toOrderDetailsDTO(OrderDetailDTO order);
    List<OrderDetails> toOrderDetailsDTOList(List<OrderDetailDTO> order);
}
