package com.example.banhangapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyAddressDto {
    private String id;
    private String province;
    private String district;
    private String commune;
    private String detail;
    private String phone;
    private String name;
    private boolean isDefault;
}
