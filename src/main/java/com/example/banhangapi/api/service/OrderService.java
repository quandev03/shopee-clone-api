package com.example.banhangapi.api.service;

import com.example.banhangapi.api.entity.Order;
import com.example.banhangapi.api.globalEnum.StatusOrder;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface OrderService {
    void createNewOrder(String cartId, String addressUserId);
    void userClickBuyNowInHomePage(String productId, int quantity);
    Page<Object> getAllOrdersForAdmin();
    Object getOrderDetailsById();
    Page<Object> getAllOrdersForUser();
    Object cancelOrderForAdmin();
    Object cancelOrderForUser();
    Object confirmOrderForAdmin();
//    void readyDataToUserCreateNewOrder();
    void unChooseProductInCartToBuy(String productId);
    Object userCompleteOrderReceiver();
    void userChooseProductToBuyInCart(String cartId);
    void userClickBuyerNowOrPickInCartToOrder(String productId, int quantity);
    List<Order> getOrderStatusForUser(StatusOrder statusOrder);
}
