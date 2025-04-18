package com.example.banhangapi.api.service;

import com.example.banhangapi.api.dto.CartResponseDTO;
import com.example.banhangapi.api.entity.Cart;
import com.example.banhangapi.api.repository.CartRepository;
import com.example.banhangapi.api.request.CartRequestDTO;
import org.apache.kafka.common.protocol.types.Field;

import java.util.List;

public interface CartService {
    Cart addCart(String productId, Double quantity);
    void removeCart(String cartId);
    List<CartResponseDTO> getAllCartOfUser();
    String updateQuantityProductInCart(String productId, Double quantity);

}
