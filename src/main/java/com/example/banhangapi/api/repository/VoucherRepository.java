package com.example.banhangapi.api.repository;

import com.example.banhangapi.api.entity.Voucher;
import jakarta.validation.constraints.Min;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher, String>, JpaSpecificationExecutor<Voucher> {
    Optional<Voucher> findByVoucherCode(String voucherCode);

    List<Voucher> findAllByLimitSlotGreaterThan(@Min(-1) int limitSlotIsGreaterThan);
}
