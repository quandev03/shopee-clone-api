package com.example.banhangapi.api.service.implement;

import com.example.banhangapi.api.dto.DataReadyToBuyDTO;
import com.example.banhangapi.api.dto.OrderDetailDTO;
import com.example.banhangapi.api.entity.*;
import com.example.banhangapi.api.globalEnum.StatusOrder;
import com.example.banhangapi.api.mapper.OrderMapper;
import com.example.banhangapi.api.repository.*;
import com.example.banhangapi.api.service.OrderService;
import com.example.banhangapi.api.service.UserService;
import com.example.banhangapi.helper.handleException.ProductNotFoundException;
import com.example.banhangapi.redis.RedisServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class OrderServicerImpl implements OrderService {
    long redisExpireTime = 15*60*1000;


    @Autowired
    RedisServiceImpl redisService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderMapper orderMapper;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    MyAddressRepository myAddressRepository;
    @Autowired
    UserServiceImple userService;




    @Override
    @SneakyThrows
    public void createNewOrder(String cartId, String addressUserIdd){
        Cart cart = cartRepository.findById(cartId).orElseThrow(()-> new ProductNotFoundException("Product not found"));
        AddressUser addressUser  = myAddressRepository.findById(addressUserIdd).orElseThrow(()-> new ProductNotFoundException("Product not found"));
        Order order = new Order();
        order.setStatusOrder(StatusOrder.ORDER_WAITING_FOR_CONFIRMATION);
        order.setAddressUser(addressUser);
        order.setProductEntity(cart.getProduct());
        order.setQuantity( cart.getQuantityBuy());
        try{
            orderRepository.save(order);
        }catch (Exception e){
            throw new ProductNotFoundException("Product not found");
        }

        log.info("Successfully created new order");
    }
    @Override
    @SneakyThrows
    public void userClickBuyNowInHomePage(String productId, int quantity){
        Cart newCart = new Cart();
        ProductEntity newProductEntity = productRepository.findById(productId).orElseThrow();
        newCart.setProduct(newProductEntity);
        newCart.setQuantityBuy(quantity);
        cartRepository.save(newCart);
    }
    public Page<Object> getAllOrdersForAdmin(){
        return null;
    };
    public Object getOrderDetailsById(){
        return null;
    };
    public Page<Object> getAllOrdersForUser(){
        return null;
    };
    public Object cancelOrderForAdmin(){
        return null;
    };
    public Object cancelOrderForUser(){
        return null;
    };
    public Object confirmOrderForAdmin(){
        return null;
    };

//    @Override
//    @SneakyThrows
//    public void readyDataToUserCreateNewOrder(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
//        String redisKeyDataReadyBuy = "redis-key-data-ready-buy-user-" + authentication.getName();
//        List<Cart> cartList = cartRepository.findAllByCheckedAndCreatedBy(true, user);
//        List<OrderDetails> orderDetails = new ArrayList<>();
//        cartList.forEach(cart -> {
//            OrderDetails orderDetail = new OrderDetails();
//            orderDetail.setProduct(cart.getProduct());
//            orderDetail.setQuantityBuy(cart.getQuantityBuy());
//            orderDetail.setTotalPrice(cart.getTotalPrice());
//            orderDetails.add(orderDetail);
//        });
//        String dataReadyToBuyJson = objectMapper.writeValueAsString(orderDetails);
//        redisService.saveData(redisKeyDataReadyBuy, dataReadyToBuyJson, redisExpireTime);
//        log.info(dataReadyToBuyJson);
//    };

    @Override
    @SneakyThrows
    public void userChooseProductToBuyInCart(String cartId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        String redisKeyDataReadyBuy = "redis-key-data-ready-buy-user-" + authentication.getName();
        Cart cart = cartRepository.findById(cartId).orElseThrow();
        OrderDetails orderDetail = new OrderDetails();
        orderDetail.setProduct(cart.getProduct());
        orderDetail.setQuantityBuy(cart.getQuantityBuy());
        List<OrderDetails> orderDetailsList;
        if(redisService.hasKey(redisKeyDataReadyBuy)){
            Object dataToRedis = redisService.getData(redisKeyDataReadyBuy);
            orderDetailsList = objectMapper.convertValue(dataToRedis, new TypeReference<List<OrderDetails>>() {});
        }else {
            orderDetailsList = new ArrayList<>();
        }
        orderDetailsList.add(orderDetail);
        String dataReadyToBuyJson = objectMapper.writeValueAsString(orderDetailsList);
        redisService.saveData(redisKeyDataReadyBuy, dataReadyToBuyJson, redisExpireTime);
        log.info(dataReadyToBuyJson);
    };

    @Override
    @SneakyThrows
    public void unChooseProductInCartToBuy(String productId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        String redisKeyDataReadyBuy = "redis-key-data-ready-buy-user-" + authentication.getName();
        List<OrderDetails> orderDetailsList;
        if(redisService.hasKey(redisKeyDataReadyBuy)){
            Object dataToRedis = redisService.getData(redisKeyDataReadyBuy);
            orderDetailsList = objectMapper.convertValue(dataToRedis, new TypeReference<List<OrderDetails>>() {});
        }else {
            throw new RuntimeException("Danh sach mua rong");
        }
        ProductEntity product = productRepository.findById(productId).orElseThrow();
        orderDetailsList.removeIf(orderDetail -> orderDetail.getProduct().equals(product));
        String dataReadyToBuyJson = objectMapper.writeValueAsString(orderDetailsList);
        redisService.saveData(redisKeyDataReadyBuy, dataReadyToBuyJson, redisExpireTime);
        log.info(dataReadyToBuyJson);

    };

    public Object userCompleteOrderReceiver(){
        return null;
    };


    @Override
    @SneakyThrows
    public void userClickBuyerNowOrPickInCartToOrder(String productId, int quantity){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String redisKeyDataReadyBuy = "redis-key-data-ready-buy-user-" + authentication.getName();
        if(redisService.hasKey(redisKeyDataReadyBuy)){
            Object dataRedis = redisService.getData(redisKeyDataReadyBuy);
            DataReadyToBuyDTO dataReadyToBuyDTO = objectMapper.convertValue(dataRedis, DataReadyToBuyDTO.class);
            ProductEntity product = productRepository.findById(productId).orElseThrow(()->new ProductNotFoundException("Not found product " + productId));
            OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
            orderDetailDTO.setProduct(product);
            orderDetailDTO.setQuantityBuy(quantity);
            orderDetailDTO.setTotalPrice(Double.valueOf(product.getPrice() * quantity));
            dataReadyToBuyDTO.addProductToBuy(orderDetailDTO);

            String dataSentRedis = objectMapper.writeValueAsString(dataReadyToBuyDTO);
            redisService.saveData(redisKeyDataReadyBuy, dataSentRedis, redisExpireTime);
        }
        else {
            ProductEntity product = productRepository.findById(productId).orElseThrow(()->new ProductNotFoundException("Not found product " + productId));
            OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
            orderDetailDTO.setProduct(product);
            orderDetailDTO.setQuantityBuy(quantity);
            orderDetailDTO.setTotalPrice(Double.valueOf(product.getPrice() * quantity));
            DataReadyToBuyDTO dataReadyToBuyDTO = new DataReadyToBuyDTO();
            dataReadyToBuyDTO.addProductToBuy(orderDetailDTO);
            String dataSentRedis = objectMapper.writeValueAsString(dataReadyToBuyDTO);
            redisService.saveData(redisKeyDataReadyBuy, dataSentRedis, redisExpireTime);
        }
    }

    @Override
    public List<Order> getOrderStatusForUser(StatusOrder statusOrder) {
        User user = userService.getCurrentUser();
        if(statusOrder.equals(StatusOrder.ORDER_ALL)) {
            return orderRepository.findAllByCreatedBy(user);
        }else {
            return orderRepository.findAllByStatusOrderAndCreatedBy(statusOrder ,user);
        }
    }

    ;

    private Long sumAmountBill(List<OrderDetails> orderDetailsList){
        AtomicReference<Long> sumAmount = new AtomicReference<>(0L);
        orderDetailsList.forEach(orderDetails -> {
            sumAmount.updateAndGet(v -> (long) (v + orderDetails.getTotalPrice()));
        });
        return sumAmount.get();
    }
}
