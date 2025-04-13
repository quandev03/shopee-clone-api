package com.example.banhangapi.api.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VoucherCount {
    private Integer sumVoucherActive;
    private Integer sumVoucherUsed;
}
