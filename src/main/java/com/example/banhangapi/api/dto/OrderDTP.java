package com.example.banhangapi.api.dto;

import com.example.banhangapi.api.entity.AddressUser;
import com.example.banhangapi.api.globalEnum.StatusOrder;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OrderDTP {
    private String orderId;
    private ProductDTO productDTO;
    private int quantity;
    private AddressUser addressUser;
    private Integer statusOrder;
    private LocalDateTime createTime;
    private String orderCode;
    private float discount;
}
