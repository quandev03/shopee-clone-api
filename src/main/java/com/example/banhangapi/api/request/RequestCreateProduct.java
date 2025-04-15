package com.example.banhangapi.api.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestCreateProduct {
    private String nameProduct;
    private String description;
    @PositiveOrZero(message = "Quantity must be zero or greater")
    private Double price;
    @PositiveOrZero(message = "Quantity must be zero or greater")
    private long quantity;
    @PositiveOrZero(message = "Quantity must be zero or greater")
    private long soldQuantity = 0;
    @Nullable
    private String categoryId;
}
