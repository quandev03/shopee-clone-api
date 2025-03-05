package com.example.banhangapi.api.service;

import com.example.banhangapi.api.dto.MyAddressDto;
import com.example.banhangapi.api.entity.Address;
import com.example.banhangapi.api.entity.AddressUser;
import com.example.banhangapi.api.request.AddressRequest;
import com.example.banhangapi.api.request.MyAddressRequest;

import java.util.List;

public interface AddressService {
    Address addAddress(AddressRequest address);
    Address addDetailAddress(AddressRequest address);
    void updateAddress(AddressRequest address);
    String getAddressById(String idMyAddress);
    List<MyAddressDto> getAllMyAddress();
    void removeMyAddress(String idMyAddress);
    AddressUser addMyAddress(MyAddressRequest myAddress);
    void updateMyAddress(String idMyAddress);
    List<Address> getListProvince();
    List<Address> getListNextLevel(String province);
    List<MyAddressDto> getListMyAddressForUser();
}
