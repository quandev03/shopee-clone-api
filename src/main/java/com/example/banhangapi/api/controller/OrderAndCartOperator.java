package com.example.banhangapi.api.controller;

import com.example.banhangapi.api.globalEnum.StatusOrder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("cart-order/")
public interface OrderAndCartOperator {
    @PostMapping("add-product-in-cart")
    ResponseEntity<Object> addNewCart(@RequestParam("productId") String productId, @RequestParam("quantity") Double quantity);

    @GetMapping("get-data-cart")
    ResponseEntity<Object> getCart();

    @PutMapping("update-cart")
    ResponseEntity<Object> updateCart(@RequestParam("productId") String productId, @RequestParam("quantity") Double quantity);

    @DeleteMapping("remove-cart")
    ResponseEntity<Object> deleteCart(@RequestParam("cartId") String cartId);

    @PostMapping("create-order")
    ResponseEntity<Object> createOrder(@RequestParam("cartId") String cartId, @RequestParam("addressUserId") String addressUserId, @RequestParam(value = "voucherCode", required = false) String voucherCode);

    @GetMapping("get-order-by-status")
    ResponseEntity<?> getOrderByStatus(@RequestParam("orderStatus") String orderStatus);

    @PutMapping("change-status-order")
    ResponseEntity<Object> changeOrderStatus(@RequestParam("orderStatus") String orderStatus, @RequestParam("orderId") String orderId);

    @PutMapping("rate-product")
    ResponseEntity<Object> rateProduct(@RequestParam("productId") String orderId, @RequestParam("rate") Integer rate);
}
