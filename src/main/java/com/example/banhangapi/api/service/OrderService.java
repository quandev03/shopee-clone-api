package com.example.banhangapi.api.service;

import com.example.banhangapi.api.dto.OrderDTP;
import com.example.banhangapi.api.entity.Order;
import com.example.banhangapi.api.globalEnum.StatusOrder;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public interface OrderService {
    void createNewOrder(String cartId, String addressUserId, String voucherCode);
    void userClickBuyNowInHomePage(String productId, Double quantity);
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
    Page<OrderDTP> getOrderAdmin(Pageable pageable, String dateFrom, String dateTo);
    void updateOrderStatus(String orderId, String statusOrderStr);
}
