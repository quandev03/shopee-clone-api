package com.example.banhangapi.api.service.implement;

import com.example.banhangapi.api.dto.AddressDTO;
import com.example.banhangapi.api.dto.AddressDTO2;
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
import jakarta.persistence.Tuple;
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
            throw new Exception("Bạn đã nhập thiếu địa chỉ trước đó");
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
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(()->new RuntimeException("Không tìm thấy"));
        List<AddressUser> listMyAddress = myAddressRepository.findByCreatedBy(user);
        List<MyAddressDto> myAddressDtoList;
        myAddressDtoList = new ArrayList<>();

        return myAddressDtoList;
    }
    @Override
    @SneakyThrows
    public void removeMyAddress(String id){
        AddressUser address = myAddressRepository.findById(id).orElseThrow(()->new RuntimeException("Không tìm thấy địa "));
        myAddressRepository.delete(address);
    }
    @Override
    @SneakyThrows
    @Transactional
    public AddressUser addMyAddress(MyAddressRequest myAddress){
        try{
            log.info("Step 2: get data new address");
            Address province = addressRepository.findById(myAddress.getProvincialAddress()).orElseThrow(()->new RuntimeException("Không tìm thấy địa chỉ"));
            Address district = addressRepository.findById(myAddress.getDistrictId()).orElseThrow(()->new RuntimeException("Không tìm thấy địa chỉ"));
            Address commercal = addressRepository.findById(myAddress.getCommercalAddress()).orElseThrow(()-> new RuntimeException("Không tìm thấy địa chỉ"));
            Address specail= null;
            if(myAddress.getSpecailAddress() != null){
                specail = addressRepository.findById(myAddress.getSpecailAddress()).orElse(null);
                if(specail==null){
                    AddressRequest newSpecailAddress = new AddressRequest(myAddress.getSpecailAddress(), AddressLever.SPECIAL, myAddress.getCommercalAddress());
                    addDetailAddress(newSpecailAddress);
                }

            }
            AddressUser addressUser = new AddressUser();
            addressUser.setProvincialAddress(province);
            addressUser.setDistrictAddress(district);
            addressUser.setCommercalAddress(commercal);
            addressUser.setSpecailAddress(specail);
            addressUser.setDetailAddress(myAddress.getDetailAddress());
            addressUser.setPhone(myAddress.getPhone());
            addressUser.setFullName(myAddress.getFullName());
            addressUser.setDefaultAddress(myAddress.isDefaultAddress());
            return myAddressRepository.save(addressUser);
        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }
    public void updateMyAddress(String idMyAddress, MyAddressRequest myAddress){
        AddressUser addressUser = myAddressRepository.findById(idMyAddress).orElseThrow(()->new RuntimeException("Không tìm thấy địa chỉ"));
        addressUser.setDefaultAddress(myAddress.isDefaultAddress());
        addressUser.setFullName(myAddress.getFullName());
        addressUser.setPhone(myAddress.getPhone());
        Address addressCom = addressRepository.findById(myAddress.getCommercalAddress()).orElseThrow(()->new RuntimeException("Không tìm thấy địa "));
        addressUser.setCommercalAddress(addressCom);
        addressUser.setDetailAddress(myAddress.getDetailAddress());
        Address addressPro = addressRepository.findById(myAddress.getDistrictId()).orElseThrow(()->new RuntimeException("Không tìm thấy địa "));
        Address addressDis = addressRepository.findById(myAddress.getProvincialAddress()).orElseThrow(()->new RuntimeException("Không tìm thấy địa "));
        addressUser.setDistrictAddress(addressDis);
        addressUser.setPhone(myAddress.getPhone());
        addressUser.setProvincialAddress(addressPro);
        myAddressRepository.save(addressUser);
    }

    @Override
    @Transactional(readOnly = true)
    @SneakyThrows
    public List<AddressDTO> getListProvince(){
        return addressRepository.findAllByAddressLevel(AddressLever.PROVINCIAL).stream().map(addressMapper::toAddressDTO).toList();
    }
    @Override
    @Transactional(readOnly = true)
    @SneakyThrows
    public List<AddressDTO> getListNextLevel(String beforeLevel){
        Address address = addressRepository.findById(beforeLevel).orElseThrow(()->new RuntimeException("Không tìm thấy địa "));
        return addressRepository.findAllByBeforeLevel(address).stream().map(addressMapper::toAddressDTO).toList();
    }

    @Override
    @SneakyThrows
    public Address addDetailAddress(AddressRequest address){
        if(address.getBeforeAddressId().isBlank() && !address.getAddressLevel().equals(AddressLever.PROVINCIAL)){
            throw new Exception("If address before address lever is provincial");
        }
        log.info("Step 1: get data new address");
        Address newAddress = addressMapper.toAddress(address);
        if(address.getBeforeAddressId() != null && !address.getBeforeAddressId().isBlank()){
            Address beforeAddress = addressRepository.findById(address.getBeforeAddressId()).orElseThrow(()->new RuntimeException("Không tìm thấy địa chỉ"));
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
        return new MyAddressDto(addressUser.getId(),addressUser.getProvincialAddress().getNameAddress(), addressUser.getDistrictAddress().getNameAddress(), addressUser.getCommercalAddress().getNameAddress(), addressUser.getDetailAddress(), addressUser.getPhone(), addressUser.getFullName(), false );
    }
}
