package com.example.banhangapi.api.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;


@Data
@AllArgsConstructor
public class MyAddressRequest {
    @NotNull
    private String provincialAddress;
    @NotNull
    private String districtId;
    @NotNull
    private String commercalAddress;
    private String specailAddress;
    private String detailAddress;
    @Nullable
    private boolean defaultAddress;
    @NotNull
    private String phone;
    private String fullName;

}
