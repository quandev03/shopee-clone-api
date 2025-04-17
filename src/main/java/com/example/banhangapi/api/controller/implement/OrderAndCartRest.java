package com.example.banhangapi.api.controller.implement;

import com.example.banhangapi.api.controller.OrderAndCartOperator;
import com.example.banhangapi.api.dto.OrderDTP;
import com.example.banhangapi.api.entity.Order;
import com.example.banhangapi.api.globalEnum.StatusOrder;
import com.example.banhangapi.api.mapper.OrderMapper;
import com.example.banhangapi.api.repository.OrderRepository;
import com.example.banhangapi.api.service.CartService;
import com.example.banhangapi.api.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class OrderAndCartRest implements OrderAndCartOperator {

    private final CartService cartService;
    private final OrderService orderService;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    @Override
    public ResponseEntity<Object> addNewCart(String productId, Double quantity) {

        return ResponseEntity.ok(cartService.addCart(productId, quantity));
    }

    @Override
    public ResponseEntity<Object> getCart() {
        return ResponseEntity.ok(cartService.getAllCartOfUser());
    }

    @Override
    public ResponseEntity<Object>  updateCart(String productId, Double quantity) {
        try{
            cartService.updateQuantityProductInCart(productId, quantity);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public ResponseEntity<Object> deleteCart(String cartId) {
        cartService.removeCart(cartId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Object> createOrder(String cartId, String addressUserId, String voucherCode) {
        orderService.createNewOrder(cartId, addressUserId, voucherCode);
        return ResponseEntity.ok("Success");
    }

    @Override
    public ResponseEntity<?> getOrderByStatus(String orderStatus) {
        Integer statusI = Integer.valueOf(orderStatus);
        StatusOrder status = StatusOrder.fromInt(statusI);
        List<Order> listOrder =orderService.getOrderStatusForUser(status);
        return ResponseEntity.ok(listOrder.stream().map(orderMapper::toOrderDTPDTOList));
    }

    @Override
    public ResponseEntity<Object> changeOrderStatus(String orderStatus, String orderId) {
        orderService.updateOrderStatus(orderId, orderStatus);
        return ResponseEntity.ok("Success");
    }

    @Override
    public ResponseEntity<Object> rateProduct(String orderId, Integer rate) {
        orderService.rateProduct(orderId, rate);
        return ResponseEntity.ok("Success");
    }


}
