package com.example.banhangapi.api.service.implement;

import com.example.banhangapi.api.dto.MyAddressDto;
import com.example.banhangapi.api.entity.Address;
import com.example.banhangapi.api.entity.AddressUser;
import com.example.banhangapi.api.entity.User;
import com.example.banhangapi.api.globalEnum.AddressLever;
import com.example.banhangapi.api.mapper.AddressMapper;
import com.example.banhangapi.api.mapper.MyAddressMapper;
import com.example.banhangapi.api.repository.AddressRepository;
import com.example.banhangapi.api.repository.MyAddressRepository;
import com.example.banhangapi.api.repository.UserRepository;
import com.example.banhangapi.api.request.AddressRequest;
import com.example.banhangapi.api.request.MyAddressRequest;
import com.example.banhangapi.api.service.AddressService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    @Autowired
    private MyAddressRepository myAddressRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private MyAddressMapper myAddressMapper;

    @Autowired
    private UserRepository userRepository;

    @SneakyThrows
    public Address addAddress(AddressRequest address){
        if(address.getBeforeAddressId().isBlank() && !address.getAddressLevel().equals(AddressLever.PROVINCIAL)){
            throw new Exception("If address before address lever is provincial");
        }
        log.info("Step 1: get data new address");
        Address newAddress = addressMapper.toAddress(address);
        if(address.getBeforeAddressId() != null && !address.getBeforeAddressId().isBlank()){
            Address beforeAddress = addressRepository.findById(address.getBeforeAddressId()).orElseThrow(()->new RuntimeException("No such address"));
            newAddress.setBeforeLevel(beforeAddress);
        }
        return  addressRepository.save(newAddress);
    };
    public void updateAddress(AddressRequest address){

    };
    @Override
    @SneakyThrows
    public String getAddressById(String idMyAddress){
        return null;
    };
    @Override
    @SneakyThrows
    public List<MyAddressDto> getAllMyAddress(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(()->new RuntimeException("No Found"));
        List<AddressUser> listMyAddress = myAddressRepository.findByCreatedBy(user);
        List<MyAddressDto> myAddressDtoList;
        myAddressDtoList = new ArrayList<>();


        return myAddressDtoList;
    };
    @Override
    @SneakyThrows
    public void removeMyAddress(String idMyAddress){
        AddressUser address = myAddressRepository.findById(idMyAddress).orElseThrow(()->new RuntimeException("No such address"));
        myAddressRepository.delete(address);
    };
    @Override
    @SneakyThrows
    public AddressUser addMyAddress(MyAddressRequest myAddress){
        Address province = addressRepository.findById(myAddress.getProvincialAddress()).orElseThrow(()->new RuntimeException("No such address"));
        Address district = addressRepository.findById(myAddress.getDistrictId()).orElseThrow(()->new RuntimeException("No such address"));
        Address commercal = addressRepository.findById(myAddress.getCommercalAddress()).orElseThrow(()-> new RuntimeException("No such address"));
        Address specail= null;
        Address detailAddress = null;
        if(myAddress.getSpecailAddress() != null){
            specail = addressRepository.findById(myAddress.getSpecailAddress()).orElse(null);
            if(specail==null){
                AddressRequest newSpecailAddress = new AddressRequest(myAddress.getSpecailAddress(), AddressLever.SPECIAL, myAddress.getCommercalAddress());
                addDetailAddress(newSpecailAddress);
            }

        }
        if(myAddress.getDetailAddress() != null){
            detailAddress = addressRepository.findById(myAddress.getDetailAddress()).orElse(null);
            if(specail==null){
                AddressRequest newDetailAddress = new AddressRequest(myAddress.getSpecailAddress(), AddressLever.OTHER, myAddress.getCommercalAddress());
                addDetailAddress(newDetailAddress);
            }
        }
        AddressUser addressUser = new AddressUser();
        addressUser.setProvincialAddress(province);
        addressUser.setDistrictAddress(district);
        addressUser.setCommercalAddress(commercal);
        addressUser.setSpecailAddress(specail);
        addressUser.setDetailAddress(detailAddress);
        addressUser.setNumberPhone(myAddress.getNumberPhone());
        return myAddressRepository.save(addressUser);
    };
    public void updateMyAddress(String idMyAddress){

    };

    @Override
    @Transactional(readOnly = true)
    @SneakyThrows
    public List<Address> getListProvince(){
        return addressRepository.findAllByAddressLevel(AddressLever.PROVINCIAL);
    };
    @Override
    @Transactional(readOnly = true)
    @SneakyThrows
    public List<Address> getListNextLevel(String province){
        Address provinceAddress = addressRepository.findById(province).orElseThrow(()->new RuntimeException("No such address"));
        return addressRepository.findAllByBeforeLevel(provinceAddress);
    };

    @Override
    @SneakyThrows
    public Address addDetailAddress(AddressRequest address){
        if(address.getBeforeAddressId().isBlank() && !address.getAddressLevel().equals(AddressLever.PROVINCIAL)){
            throw new Exception("If address before address lever is provincial");
        }
        log.info("Step 1: get data new address");
        Address newAddress = addressMapper.toAddress(address);
        if(address.getBeforeAddressId() != null && !address.getBeforeAddressId().isBlank()){
            Address beforeAddress = addressRepository.findById(address.getBeforeAddressId()).orElseThrow(()->new RuntimeException("No such address"));
            newAddress.setBeforeLevel(beforeAddress);
        }
        return  addressRepository.save(newAddress);
    }
    public List<MyAddressDto> getListMyAddressForUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        List<AddressUser> listMyAddress = myAddressRepository.findByCreatedBy(user);
        List<MyAddressDto> myAddressDtoList = new ArrayList<>();
        listMyAddress.forEach(addressUser -> {
            myAddressDtoList.add(convertMyAddress(addressUser));
        });
        return myAddressDtoList;
    }
    private MyAddressDto convertMyAddress(AddressUser addressUser){
       String address = "";
       if(addressUser.getDetailAddress() != null){
           address+=addressUser.getDetailAddress();
       }
       if(addressUser.getSpecailAddress() != null){
           address+= ", " + addressUser.getSpecailAddress();
       }
       address+=", " + addressUser.getCommercalAddress();
       address+=", " + addressUser.getDistrictAddress();
       address+=", " + addressUser.getProvincialAddress();
        return new MyAddressDto(addressUser.getId(), address, addressUser.getNumberPhone());
    }
}
