package com.example.banhangapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddressDTO {
    private String id;
    private String nameAddress;
    private AddressDTO beforeLevel;
}
