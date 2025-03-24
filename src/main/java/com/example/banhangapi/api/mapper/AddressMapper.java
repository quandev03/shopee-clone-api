package com.example.banhangapi.api.mapper;

import com.example.banhangapi.api.dto.AddressDTO;
import com.example.banhangapi.api.dto.AddressDTO2;
import com.example.banhangapi.api.entity.Address;
import com.example.banhangapi.api.request.AddressRequest;
import jakarta.persistence.Tuple;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import reactor.util.function.Tuple2;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    @Mapping(source = "addressLevel", target = "addressLevel")
    Address toAddress(AddressRequest address);
    AddressRequest toAddressRequest(Address address);

//    AddressDTO2 toAddressDTO2(Address address);
    AddressDTO toAddressDTO(Address address);
}
