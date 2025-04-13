package com.example.banhangapi.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetails implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    ProductEntity product;

    Double quantityBuy;

    @Min(value = 1)
    @Max(value = 5)
    int rateProduct;

    @Min(value = 0)
    Double totalPrice;

    @ManyToOne(fetch = FetchType.EAGER)
    Order order;
}
