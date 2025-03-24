package com.example.banhangapi.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class InforMeDTO {
    private String id;
    private String username;
    private String birthday;
    private String avatar;
    private String phone;
    private String fullName;
    private List<MyAddressDto> addressList;
}
