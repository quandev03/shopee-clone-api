package com.example.banhangapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyAddressDto {
    private String id;
    private String address;
    private String phoneNumber;
}
