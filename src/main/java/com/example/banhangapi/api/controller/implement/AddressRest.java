package com.example.banhangapi.api.controller.implement;

import com.example.banhangapi.api.controller.AddressOperator;
import com.example.banhangapi.api.request.AddressRequest;
import com.example.banhangapi.api.request.MyAddressRequest;
import com.example.banhangapi.api.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddressRest implements AddressOperator {

    @Autowired
    private AddressService addressService;

    @Override
    public ResponseEntity<Object> addNewAddress(AddressRequest address) {
        return ResponseEntity.ok(addressService.addAddress(address));
    }
    @Override
    public ResponseEntity<Object> getListProvince(){
        return ResponseEntity.ok(addressService.getListProvince());
    }
    @Override
    public ResponseEntity<Object> getListNextLever(String province){
        return ResponseEntity.ok(addressService.getListNextLevel(province));
    }
    @Override
    public ResponseEntity<Object> addNewMyAddress(@RequestBody MyAddressRequest address){
        return ResponseEntity.ok(addressService.addMyAddress(address));
    }
    @Override
    public ResponseEntity<Object> getListMyAddress(){
        return ResponseEntity.ok(addressService.getListMyAddressForUser());
    }
    @Override
    public ResponseEntity<String> deleteMyAddress(String id){
        addressService.removeMyAddress(id);
        return ResponseEntity.ok("Delete My Address Success");
    }


}
