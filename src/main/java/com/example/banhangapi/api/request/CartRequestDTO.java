package com.example.banhangapi.api.request;

import com.example.banhangapi.api.dto.CommentDTO;
import com.example.banhangapi.api.dto.ProductDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartRequestDTO {
    private String productId;
    private int quantityCart;
}
