package com.example.banhangapi.api.controller.implement;

import com.example.banhangapi.api.controller.OrderAndCartOperator;
import com.example.banhangapi.api.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderAndCartRest implements OrderAndCartOperator {
    private final CartService cartService;
    @Override
    public ResponseEntity<Object> addNewCart(String productId, int quantity) {

        return ResponseEntity.ok(cartService.addCart(productId, quantity));
    }

    @Override
    public ResponseEntity<Object> getCart() {
        return ResponseEntity.ok(cartService.getAllCartOfUser());
    }
}
