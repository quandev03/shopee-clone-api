package com.example.banhangapi.api.mapper;

import com.example.banhangapi.api.dto.VoucherDTO;
import com.example.banhangapi.api.entity.Voucher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VoucherMapper {
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "expirationDate", target = "expirationDate")
    VoucherDTO toVoucherDTO(Voucher voucher);
}
