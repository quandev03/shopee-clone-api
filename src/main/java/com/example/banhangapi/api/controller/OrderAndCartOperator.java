package com.example.banhangapi.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("cart-order/")
public interface OrderAndCartOperator {
    @PostMapping("add-product-in-cart")
    ResponseEntity<Object> addNewCart(@RequestParam("productId") String productId, @RequestParam("quantity") int quantity);
}
