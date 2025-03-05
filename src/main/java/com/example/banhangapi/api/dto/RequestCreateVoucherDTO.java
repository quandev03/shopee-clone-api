package com.example.banhangapi.api.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestCreateVoucherDTO {
    @Size(min = 0, max = 100)
    int discount;
    @Nullable
    @Size(min = 5, max = 10)
    String voucherCode;

    String description;
    String startDate;
    String expirationDate;
    int limitSlot;

    private boolean isLimitedUsage;
    int slotUsage =0;
}
