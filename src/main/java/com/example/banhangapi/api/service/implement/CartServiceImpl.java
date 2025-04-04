package com.example.banhangapi.api.service.implement;

import com.example.banhangapi.api.dto.CartResponseDTO;
import com.example.banhangapi.api.entity.Cart;
import com.example.banhangapi.api.entity.ProductEntity;
import com.example.banhangapi.api.entity.User;
import com.example.banhangapi.api.mapper.CartMapper;
import com.example.banhangapi.api.repository.CartRepository;
import com.example.banhangapi.api.repository.ProductRepository;
import com.example.banhangapi.api.repository.UserRepository;
import com.example.banhangapi.api.service.CartService;
import com.example.banhangapi.api.service.UserService;
import com.example.banhangapi.helper.handleException.ProductNotFoundException;
import com.example.banhangapi.helper.handleException.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartServiceImpl implements CartService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private UserServiceImple userService;

    @Override
    @SneakyThrows
    public Cart addCart(String productId, int quantity){
        ProductEntity product = productRepository.findById(productId).orElseThrow(()-> new ProductNotFoundException("Product not found"));
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()-> new UserNotFoundException("User not found"));
        if(cartRepository.existsByProductAndCreatedBy(product, user)){
            throw new RuntimeException("Cart already exists");
        }
        Cart newCart = Cart.builder()
                .product(product)
                .quantityBuy(quantity)
                .build();
        return cartRepository.save(newCart);
    };

    @Override
    @SneakyThrows
    public void removeCart(String cartId){
        Cart cartRemove = cartRepository.findById(cartId).orElseThrow(()->new ProductNotFoundException("Not found Product in cart"));
        cartRepository.delete(cartRemove);
    };

    @Override
    @SneakyThrows
    public List<CartResponseDTO> getAllCartOfUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(()-> new UserNotFoundException("User not found"));
        return cartRepository.findByCreatedBy(user)
                .stream()
                .map(cartMapper::toCartResponseDTO) // Sử dụng cartMapper để chuyển đổi từ Cart sang CartResponseDTO
                .toList();
    }

    @Transactional
    public String updateQuantityProductInCart(String cartId, int quantity) {
        cartRepository.updateCart(cartId, quantity);
        return "Success";
    }
}
