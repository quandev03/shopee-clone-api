package com.example.banhangapi.api.service.implement;

import com.example.banhangapi.api.dto.*;
import com.example.banhangapi.api.entity.OrderDetails;
import com.example.banhangapi.api.globalEnum.StatusOrder;
import com.example.banhangapi.api.mapper.OrderMapper;
import com.example.banhangapi.api.mapper.ProductMapper;
import com.example.banhangapi.api.repository.*;
import com.example.banhangapi.api.service.AdminShop;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminShopImpl implements AdminShop {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final UserHistoryOfvoucherUsageRepository userHistoryOfvoucherUsageRepository;
    private final VoucherRepository voucherRepository;
    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;

    @Override
    public DashboardDTO getDashboard() {
        DashboardDTO response = new DashboardDTO();
        Double orderTotal = orderRepository.getTotalAmountInDash();
        Integer totalOrders = orderRepository.getTotalOrders();
        Integer totalUser = userRepository.totalUser();
        response.setTotalRevenue(orderTotal);
        response.setTotalOrders(totalOrders);
        response.setTotalUser(totalUser);
        response.setTotalProducts(productRepository.countProduct());
        VoucherCount voucherCount = new VoucherCount(voucherRepository.countVoucherActive(), userHistoryOfvoucherUsageRepository.countRecordsCreatedInCurrentMonth());
        response.setVoucherCount(voucherCount);
        List<ProductOutOfStock> productOutOfStockList = productRepository.productOutOfStockLong().stream().map(productMapper::toProductOutOfStock).toList();
        response.setProductOutOfStockLong(productOutOfStockList);
        List<OrderDTP> listOrderRecent = orderRepository.findTop5RecentOrders().stream().map(orderMapper::toOrderDTPDTOList).toList();
        response.setRecentOrders(listOrderRecent);
        LocalDateTime startDate = LocalDateTime.now().minusDays(6);
        List<Object[]> revenue7DayRecentRes = orderRepository.getDailyRevenueForLast7Days(startDate);
        List<Revenue7DayRecent> revenue7DayRecentList = revenue7DayRecentRes.stream()
                .map(object -> new Revenue7DayRecent((Double) object[0], (String) object[1]))
                .collect(Collectors.toList());
        response.setRevenue7DayRecent(revenue7DayRecentList);
        return response;

    }
}
