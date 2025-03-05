package com.example.banhangapi.api.mapper;

import com.example.banhangapi.api.entity.Address;
import com.example.banhangapi.api.request.AddressRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    @Mapping(source = "addressLevel", target = "addressLevel")
    Address toAddress(AddressRequest address);
    AddressRequest toAddressRequest(Address address);
}
