package com.example.banhangapi.api.controller.implement;

import com.example.banhangapi.api.controller.AdminManagerController;
import com.example.banhangapi.api.dto.RequestCreateVoucherDTO;
import com.example.banhangapi.api.dto.VoucherDTO;
import com.example.banhangapi.api.entity.Order;
import com.example.banhangapi.api.entity.ProductEntity;
import com.example.banhangapi.api.globalEnum.StatusOrder;
import com.example.banhangapi.api.globalEnum.TypeThisTimeTotalAmount;
import com.example.banhangapi.api.service.ManagerVoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminManagerControllerImple implements AdminManagerController {
    private final ManagerVoucherService managerVoucherService;

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
    public ResponseEntity<Page<ProductEntity>> getAllProductModeAdmin(){
        return null;
    };
}
