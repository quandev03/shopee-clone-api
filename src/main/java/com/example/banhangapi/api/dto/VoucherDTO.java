package com.example.banhangapi.api.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherDTO {
    float discount;
    String voucherCode;
    String description;
    String startDate;
    String expirationDate;
    int limitSlot;
    String remainingTime;
    String id;


}
