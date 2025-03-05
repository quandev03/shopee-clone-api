package com.example.banhangapi.api.request;

import com.example.banhangapi.api.entity.Address;
import com.example.banhangapi.api.globalEnum.AddressLever;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddressRequest {
    private String nameAddress;
    private AddressLever addressLevel;
    @Nullable
    private String beforeAddressId;
}
