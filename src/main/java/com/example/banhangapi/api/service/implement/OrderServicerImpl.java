package com.example.banhangapi.api.service.implement;

import com.example.banhangapi.api.dto.OrderDTP;
import com.example.banhangapi.api.entity.*;
import com.example.banhangapi.api.globalEnum.StatusOrder;
import com.example.banhangapi.api.mapper.OrderMapper;
import com.example.banhangapi.api.repository.*;
import com.example.banhangapi.api.service.CartService;
import com.example.banhangapi.api.service.OrderService;
import com.example.banhangapi.helper.handleException.ProductNotFoundException;
import com.example.banhangapi.redis.RedisServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
@RequiredArgsConstructor
public class OrderServicerImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final CartRepository cartRepository;
    private final MyAddressRepository myAddressRepository;
    private final UserServiceImple userService;
    private final VoucherRepository voucherRepository;
    private final UserHistoryOfvoucherUsageRepository voucherUsageRepository;
    private final CartService cartService;
    private final HistoryRateRepository historyRateRepository;



    @Override
    @SneakyThrows
    public void createNewOrder(String cartId, String addressUserId, String voucherCode){
        Cart cart = cartRepository.findById(cartId).orElseThrow(()-> new ProductNotFoundException("Không tìm thấy sản phẩm"));
        AddressUser addressUser  = myAddressRepository.findById(addressUserId).orElseThrow(()-> new ProductNotFoundException("Không tìm thấy địa chỉ"));
        Voucher voucher;
        Order order = new Order();
        order.setAddressUser(addressUser);
        order.setProductEntity(cart.getProduct());
        order.setQuantity( cart.getQuantityBuy());
        order.setOrderCode(genCodeOrder());
        if(voucherCode!=null){
            voucher= voucherRepository.findByVoucherCode(voucherCode).orElseThrow(()-> new ProductNotFoundException("Không tìm thấy voucher"));
            if (Boolean.TRUE.equals(voucher.getLimitedUsage())){
                User user = userService.getCurrentUser();
                int countUse = voucherUsageRepository.countSlotUseVoucherByUser(user.getId(), voucherCode);
                if(countUse >= voucher.getSlotUsageForUser()){
                    throw new RuntimeException("Mã giảm giá đã được sử dụng");
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
            throw new ProductNotFoundException("Không tìm thấy sản phẩm");
        }

        log.info("tạo đơn hàng thành công");
    }
    private String genCodeOrder(){
        Integer index = orderRepository.getOrdersCount();
        return  "HD" + String.format("%04d", index);
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
    public Page<OrderDTP> getOrderAdmin(String dateFrom, String dateTo) {
        try{
            LocalDateTime from = null,  to = null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            if(dateFrom != null && !dateFrom.isEmpty()){
                from = LocalDateTime.parse(dateFrom, formatter);
            }
            if(dateTo != null && !dateTo.isEmpty()){
                to = LocalDateTime.parse(dateTo, formatter);
            }
            Page<Order> orders = orderRepository.findOrdersByCreateTimeRange(from, to);
            return orders.map(orderMapper::toOrderDTPDTOList);
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateOrderStatus(String orderId, String statusOrderStr) {
        Integer statusOrder = convertStatus(statusOrderStr).ordinal();
        if(statusOrder.equals(StatusOrder.ORDER_SUCCESS)){
            productRepository.updateQuantityBuy(orderId);
        }
        orderRepository.updateStatus(orderId, statusOrder);
    }

    @Override
    public void rateProduct(String orderId, int rate) {



        Order order = orderRepository.findById(orderId).get();
        ProductEntity product = order.getProductEntity();

        if (historyRateRepository.checkRateOrder(orderId, order.getCreatedBy().getId()) > 0){
            throw new RuntimeException("Đơn hàng đã được đáng giá trước đó");
        }

        HistoryRate historyRate = new HistoryRate();
        historyRate.setRate(rate);
        historyRate.setProduct(product);
        historyRate.setOrderId(order);
        historyRateRepository.save(historyRate);


        int rating;
        if (product.getRating() == null){
            rating = rate;
        }else{
            rating = (int) Math.round(historyRateRepository.getRateByProductId(product.getId()));
        }
        product.setRating(rating);
        log.info("rating: {}", rating);
        productRepository.updateRating(product.getId(), rating);

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
