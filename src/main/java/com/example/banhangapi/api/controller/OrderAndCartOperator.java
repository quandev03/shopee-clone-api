package com.example.banhangapi.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("cart-order/")
@CrossOrigin(origins = "http://localhost:3000")
public interface OrderAndCartOperator {
    @PostMapping("add-product-in-cart")
    ResponseEntity<Object> addNewCart(@RequestParam("productId") String productId, @RequestParam("quantity") int quantity);

    @GetMapping("get-data-cart")
    ResponseEntity<Object> getCart();
}
