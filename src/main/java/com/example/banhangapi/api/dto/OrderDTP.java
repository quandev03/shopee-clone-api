package com.example.banhangapi.api.dto;

import com.example.banhangapi.api.entity.AddressUser;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderDTP {
    private String orderId;
    private ProductDTO productDTO;
    private int quantity;
    private AddressUser addressUser;
}
