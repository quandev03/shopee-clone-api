package com.example.banhangapi.api.controller;

import com.example.banhangapi.api.request.AddressRequest;
import com.example.banhangapi.api.request.MyAddressRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("address-manage")
public interface AddressOperator {
    @PostMapping("add-new-address")//address-manage/add-new-address
    ResponseEntity<Object> addNewAddress(@RequestBody AddressRequest address);
    @GetMapping("/get-list-province")
    ResponseEntity<Object> getListProvince();
    @GetMapping("get-list-next-level")
    ResponseEntity<Object> getListNextLever(@RequestParam(value = "next-level") String province);
    @PostMapping("add-new-my-address")
    ResponseEntity<Object> addNewMyAddress(@RequestBody MyAddressRequest address);
    @GetMapping("get-list-my-address")
    ResponseEntity<Object> getListMyAddress();
    @DeleteMapping("delete-mu-address")
    ResponseEntity<String> deleteMyAddress(@RequestParam(value = "id") String id);
}
