package com.example.banhangapi.api.service;

import org.apache.kafka.common.protocol.types.Field;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


public interface OrderService {
    void createNewOrder();
    void userClickBuyNowInHomePage(String productId, int quantity);
    Page<Object> getAllOrdersForAdmin();
    Object getOrderDetailsById();
    Page<Object> getAllOrdersForUser();
    Object cancelOrderForAdmin();
    Object cancelOrderForUser();
    Object confirmOrderForAdmin();
    void readyDataToUserCreateNewOrder();
    void unChooseProductInCartToBuy(String productId);
    Object userCompleteOrderReceiver();
    void userChooseProductToBuyInCart(String cartId);
    void userClickBuyerNowOrPickInCartToOrder(String productId, int quantity);
}
