package com.example.banhangapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataReadyToBuyDTO {
    private List<OrderDetailDTO> orderDetailDTOList;
    private Double totalPrice;
    public void addProductToBuy(OrderDetailDTO orderDetailDTO) {
        this.totalPrice = this.totalPrice + orderDetailDTO.totalPrice;
        orderDetailDTOList.add(orderDetailDTO);
    }
    public void removeProductFromBuy(OrderDetailDTO orderDetailDTO) {
        this.totalPrice = this.totalPrice - orderDetailDTO.totalPrice;
        orderDetailDTOList.remove(orderDetailDTO);
    }
}
