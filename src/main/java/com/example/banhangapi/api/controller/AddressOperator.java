package com.example.banhangapi.api.controller;

import com.example.banhangapi.api.request.AddressRequest;
import com.example.banhangapi.api.request.MyAddressRequest;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("address-manage")

public interface AddressOperator {
    @Operation(summary = "Thêm địa chỉ",
            description = "Thêm 1 địa chỉ ")
    @PostMapping("add-new-address")//address-manage/add-new-address
    ResponseEntity<Object> addNewAddress(@RequestBody AddressRequest address);


    @GetMapping("/get-list-province")
    @Operation(summary = "Lấy danh sách tỉnh",
    description = "Lấy danh sách các tỉnh")
    ResponseEntity<Object> getListProvince();

    @Operation(summary = "Lấy danh sách địa chỉ tiếp theo",
            description = "Lấy danh sách địa chỉ có level nhỏ hơn, dựa vào địa chỉ trước đó")
    @GetMapping("get-list-next-level")
    ResponseEntity<Object> getListNextLever(@RequestParam() String beforeLevel);

    @PostMapping("add-new-my-address")
    ResponseEntity<Object> addNewMyAddress(@RequestBody MyAddressRequest address);
    @GetMapping("get-my-address")
    ResponseEntity<Object> getListMyAddress();
    @DeleteMapping("delete-my-address")
    ResponseEntity<String> deleteMyAddress(@RequestParam(value = "id") String id);

    @PutMapping("update-my-address")
    ResponseEntity<Object> updateMyAddress(@RequestBody MyAddressRequest address, @RequestParam String addressId);
}
