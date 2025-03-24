package com.example.banhangapi.api.dto;

import com.example.banhangapi.api.entity.ProductEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartResponseDTO {
    String id;
    ProductDTO product;
    int quantity;
}
