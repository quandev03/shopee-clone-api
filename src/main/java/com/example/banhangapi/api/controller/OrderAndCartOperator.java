package com.example.banhangapi.api.controller;

import com.example.banhangapi.api.globalEnum.StatusOrder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("cart-order/")
@CrossOrigin(origins = "http://localhost:3000")
public interface OrderAndCartOperator {
    @PostMapping("add-product-in-cart")
    ResponseEntity<Object> addNewCart(@RequestParam("productId") String productId, @RequestParam("quantity") int quantity);

    @GetMapping("get-data-cart")
    ResponseEntity<Object> getCart();

    @PutMapping("update-cart")
    ResponseEntity<Object> updateCart(@RequestParam("productId") String productId, @RequestParam("quantity") int quantity);

    @DeleteMapping("remove-cart")
    ResponseEntity<Object> deleteCart(@RequestParam("cartId") String cartId);

    @PostMapping("create-order")
    ResponseEntity<Object> createOrder(@RequestParam("cartId") String cartId, @RequestParam("addressUserId") String addressUserId);

    @GetMapping("get-order-by-status")
    ResponseEntity<?> getOrderByStatus(@RequestParam("orderStatus") String orderStatus);
}
