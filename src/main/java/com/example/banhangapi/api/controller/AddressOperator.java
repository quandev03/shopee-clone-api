package com.example.banhangapi.api.controller;

import com.example.banhangapi.api.request.AddressRequest;
import com.example.banhangapi.api.request.MyAddressRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("address-manage")
@CrossOrigin(origins = "http://0.0.0.0:3000")
public interface AddressOperator {
    @PostMapping("add-new-address")//address-manage/add-new-address
    ResponseEntity<Object> addNewAddress(@RequestBody AddressRequest address);
    @GetMapping("/get-list-province")
    ResponseEntity<Object> getListProvince();
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
