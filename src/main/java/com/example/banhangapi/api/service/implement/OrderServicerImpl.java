package com.example.banhangapi.api.service.implement;

import com.example.banhangapi.api.dto.DataReadyToBuyDTO;
import com.example.banhangapi.api.dto.OrderDTP;
import com.example.banhangapi.api.dto.OrderDetailDTO;
import com.example.banhangapi.api.entity.*;
import com.example.banhangapi.api.globalEnum.StatusOrder;
import com.example.banhangapi.api.mapper.OrderMapper;
import com.example.banhangapi.api.repository.*;
import com.example.banhangapi.api.service.CartService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Autowired
    VoucherRepository voucherRepository;

    @Autowired
    UserHistoryOfvoucherUsageRepository voucherUsageRepository;

    @Autowired
    CartService cartService;



    @Override
    @SneakyThrows
    public void createNewOrder(String cartId, String addressUserId, String voucherCode){
        Cart cart = cartRepository.findById(cartId).orElseThrow(()-> new ProductNotFoundException("Product not found"));
        AddressUser addressUser  = myAddressRepository.findById(addressUserId).orElseThrow(()-> new ProductNotFoundException("Product not found"));
        Voucher voucher;
        Order order = new Order();
        order.setAddressUser(addressUser);
        order.setProductEntity(cart.getProduct());
        order.setQuantity( cart.getQuantityBuy());
        order.setOrderCode(genCodeOrder());
        if(voucherCode!=null){
            voucher= voucherRepository.findByVoucherCode(voucherCode).orElseThrow(()-> new ProductNotFoundException("Voucher not found"));
            if (Boolean.TRUE.equals(voucher.getLimitedUsage())){
                User user = userService.getCurrentUser();
                int countUse = voucherUsageRepository.countSlotUseVoucherByUser(user.getId(), voucherCode);
                if(countUse >= voucher.getSlotUsageForUser()){
                    throw new RuntimeException("Voucher already used");
                }
            }
            order.setVoucherCode(voucher.getVoucherCode());
            order.setDiscount(voucher.getDiscount());
            UserHistoryOfvoucherUsage userHistoryOfvoucherUsage = new UserHistoryOfvoucherUsage();
            userHistoryOfvoucherUsage.setVoucher(voucher);
            voucherRepository.useVoucher(voucherCode);
            voucherUsageRepository.save(userHistoryOfvoucherUsage);
        }
        try{
            orderRepository.save(order);
            cartService.removeCart(cartId);
        }catch (Exception e){
            throw new ProductNotFoundException("Product not found");
        }

        log.info("Successfully created new order");
    }
    private String genCodeOrder(){
        Integer index = orderRepository.getOrdersCount();
        return  "HD" + String.format("%04d", index);
    }
    @Override
    @SneakyThrows
    public void userClickBuyNowInHomePage(String productId, Double quantity){
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
            return orderRepository.findAllByStatusOrderAndCreatedBy(statusOrder.ordinal() ,user);
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
    public Page<OrderDTP> getOrderAdmin(Pageable pageable, String dateFrom, String dateTo) {
        try{
            LocalDateTime from = null,  to = null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            if(dateFrom != null && !dateFrom.isEmpty()){
                from = LocalDateTime.parse(dateFrom, formatter);
            }
            if(dateTo != null && !dateTo.isEmpty()){
                to = LocalDateTime.parse(dateTo, formatter);
            }
            Page<Order> orders = orderRepository.findOrdersByCreateTimeRange(from, to, pageable);
            return orders.map(order -> orderMapper.toOrderDTPDTOList(order));
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateOrderStatus(String orderId, String statusOrderStr) {
        Integer statusOrder = convertStatus(statusOrderStr).ordinal();
        orderRepository.updateStatus(orderId, statusOrder);
    }
    private StatusOrder convertStatus(String statusOrderStr){
        switch (statusOrderStr){
            case "confirmed":
                return StatusOrder.ORDER_PACKING_GOODS;
            case "delivered":
                return StatusOrder.ORDER_SUCCESS;
            case "cancelled":
                return StatusOrder.ORDER_CANCEL;
            case "shipping":
                return StatusOrder.ORDER_TRANSPORT_GOODS;
            default:
                return StatusOrder.ORDER_WAITING_FOR_CONFIRMATION;
        }
    }
}
