package com.example.banhangapi.api.dto;

import com.example.banhangapi.api.entity.ProductEntity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import javax.annotation.Nullable;
@Data
public class OrderDetailDTO {
    String id;

    ProductEntity product;

    Integer quantityBuy;

    @Nullable
    @Min(value = 1)
    @Max(value = 5)
    int rateProduct;

    Double totalPrice;
}
