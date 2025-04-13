package com.example.banhangapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDTO {
    // Tong doanh thu
    private Double totalRevenue;
    //Tổng đơn hàng
    private Integer totalOrders;
    // Tong so nguoi dung
    private Integer totalUser;
    //Tong san pham
    private Integer totalProducts;
    // Thong ke voucher
    private VoucherCount voucherCount;
    private List<ProductOutOfStock> productOutOfStockLong;
    private List<OrderDTP> recentOrders;
    private List<Revenue7DayRecent> revenue7DayRecent;

}
