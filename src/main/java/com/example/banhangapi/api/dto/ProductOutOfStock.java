package com.example.banhangapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductOutOfStock {
    private String productId;
    private String nameProduct;
    private int quantity;
    private String imageUrl;
}
