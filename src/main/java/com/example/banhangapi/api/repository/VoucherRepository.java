package com.example.banhangapi.api.repository;

import com.example.banhangapi.api.entity.Voucher;
import jakarta.validation.constraints.Min;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher, String>, JpaSpecificationExecutor<Voucher> {
    Optional<Voucher> findByVoucherCode(String voucherCode);
    Boolean existsByVoucherCode(String voucherCode);


    List<Voucher> findAllByLimitSlotGreaterThan(@Min(-1) int limitSlotIsGreaterThan);

    @Query(value = """
        SELECT COUNT(*) FROM voucher v 
        WHERE v.limit_slot > 0
          AND STR_TO_DATE(v.start_date, 'yyyy-MM-dd hh:mm') < Now()
          AND STR_TO_DATE(v.expiration_date, 'yyyy-MM-dd hh:mm') > Now()
    """, nativeQuery = true)
    Integer countVoucherActive();

    @Query(value = """
        SELECT * FROM voucher v
            WHERE v.limit_slot > 0
              AND STR_TO_DATE(v.start_date, '%d/%m/%Y %H:%i:%s') < NOW()
              AND STR_TO_DATE(v.expiration_date, '%d/%m/%Y %H:%i:%s') > NOW()
    """, nativeQuery = true)
    List<Voucher> findAllValidVouchers();

//    Voucher findByVoucherCode(String voucherCode);

    @Transactional
    @Modifying
    @Query("""
        UPDATE voucher u set u.limitSlot = (u.limitSlot-1)
        WHERE u.voucherCode = :voucherCode
    """)
    void useVoucher(@Param("voucherCode") String voucherCode);
}