package com.example.banhangapi.api.controller.implement;

import com.example.banhangapi.api.controller.AdminManagerController;
import com.example.banhangapi.api.dto.*;
import com.example.banhangapi.api.entity.Order;
import com.example.banhangapi.api.entity.ProductEntity;
import com.example.banhangapi.api.globalEnum.ROLES;
import com.example.banhangapi.api.globalEnum.StatusOrder;
import com.example.banhangapi.api.globalEnum.TypeThisTimeTotalAmount;
import com.example.banhangapi.api.service.*;
import com.example.banhangapi.api.service.implement.UserServiceImple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminManagerControllerImple implements AdminManagerController {
    private final ManagerVoucherService managerVoucherService;
    private final AdminShop adminShop;
    private final ProductService productService;
    private final UserServiceImple userService;
    private final OrderService orderService;

    @Override
    public ResponseEntity<Object> createNewVoucher(RequestCreateVoucherDTO voucher) {
        return ResponseEntity.ok(managerVoucherService.createNewVoucher(voucher));
    }
    @Override
    public ResponseEntity<List<VoucherDTO>> getListVoucherCanApply(){
        return ResponseEntity.ok(managerVoucherService.getListVoucherHaveSlotLimit());
    };
    @Override
    public ResponseEntity<Page<VoucherDTO>> getAllVoucher(
            int page, int limit
    ){
        return ResponseEntity.ok(managerVoucherService.getAllVoucher(page, limit));
    };
    @Override
    public ResponseEntity<Object> changeStatusOrder(@RequestParam Long id, @RequestParam StatusOrder status){
        return null;
    };
    @Override
    public ResponseEntity<Object> getDataVoucher(String codeVoucher){
        return ResponseEntity.ok(managerVoucherService.getInfoVoucher(codeVoucher));
    }
    @Override
    public ResponseEntity<Page<Order>> getAllOrderFollowStatus(
            StatusOrder statusOrder,int page, int limit
    ){
        return null;
    };
    @Override
    public ResponseEntity<List<Order>> totalAmountShopFollowThisTime(
            TypeThisTimeTotalAmount typeThisTimeTotalAmount
    ){
        return null;
    };
    @Override
    public ResponseEntity<List<ProductDTO>> getAllProductModeAdmin(){
        return ResponseEntity.ok(productService.getAllProduct());
    }

    @Override
    public ResponseEntity<Object> getDataDashboard() {
        return ResponseEntity.ok(adminShop.getDashboard());
    }

    @Override
    public ResponseEntity<Page<UserAdminDTO>> getAllUserForAdmin(
            Integer page,
            String username,
            String fullname,
            Boolean active,
            String phone
            ) {
        Pageable pageable = PageRequest.of(page-1, 5);
        return ResponseEntity.ok(userService.getUserAdmin(pageable, active, username, fullname, phone));
    }

    @Override
    public void decentralizationAdmin(String id) {
        this.userService.decentralizationAdmin(id);
    }

    @Override
    public void decentralizationUser(String id) {
        userService.decentralizationUser(id);
    }

    @Override
    public void decentralizationCensorship(String id) {
        userService.decentralizationCensorship(id);
    }

    @Override
    public void lockUnlockAccount(String id) {
        userService.lockAndUnlockAccount(id);
    }

    @Override
    public ResponseEntity<Object> getOrderAdmin(String dateFrom, String dateTo, int page) {
        Pageable pageable = PageRequest.of(page-1, 5);
        return ResponseEntity.ok(orderService.getOrderAdmin(pageable, dateFrom, dateTo));
    }

}
