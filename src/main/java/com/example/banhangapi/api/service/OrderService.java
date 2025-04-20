package com.example.banhangapi.api.service;

import com.example.banhangapi.api.dto.OrderDTP;
import com.example.banhangapi.api.entity.Order;
import com.example.banhangapi.api.globalEnum.StatusOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;


public interface OrderService {
    void createNewOrder(String cartId, String addressUserId, String voucherCode);
    List<Order> getOrderStatusForUser(StatusOrder statusOrder);
    Page<OrderDTP> getOrderAdmin(String dateFrom, String dateTo);
    void updateOrderStatus(String orderId, String statusOrderStr);
    void rateProduct(String orderId, int rate);
}
