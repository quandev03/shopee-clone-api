package com.example.banhangapi.api.controller;

import com.example.banhangapi.api.dto.RequestCreateVoucherDTO;
import com.example.banhangapi.api.dto.VoucherDTO;
import com.example.banhangapi.api.entity.Order;
import com.example.banhangapi.api.entity.ProductEntity;
import com.example.banhangapi.api.globalEnum.StatusOrder;
import com.example.banhangapi.api.globalEnum.TypeThisTimeTotalAmount;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api-admin-manager")
public interface AdminManagerController {
    @PostMapping("create-new-voucher")
    ResponseEntity<Object> createNewVoucher(@RequestBody RequestCreateVoucherDTO voucher);

    @GetMapping("get-list-voucher-can-apply")
    ResponseEntity<List<VoucherDTO>> getListVoucherCanApply();

    @GetMapping("get-all-voucher")
    ResponseEntity<Page<VoucherDTO>> getAllVoucher(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int limit
    );

    @GetMapping("get-data-voucher")
    ResponseEntity<Object> getDataVoucher(@RequestParam(value = "code-voucher") String codeVoucher);
    @PutMapping("admin-change-status-order")

    ResponseEntity<Object> changeStatusOrder(@RequestParam Long id, @RequestParam StatusOrder status);

    @GetMapping("admin-get-all-order-follow-status")
    ResponseEntity<Page<Order>> getAllOrderFollowStatus(
            @RequestParam(required = false) StatusOrder statusOrder,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int limit
    );

    @GetMapping("total-amount-shop-follow-condition")
    ResponseEntity<List<Order>> totalAmountShopFollowThisTime(
            @RequestParam(required = false)TypeThisTimeTotalAmount typeThisTimeTotalAmount
            );

    @GetMapping("get-all-product-mode-admin")
    ResponseEntity<Page<ProductEntity>> getAllProductModeAdmin();

    @GetMapping("get-data-dashboard")
    ResponseEntity<Object> getDataDashboard();



}
