package com.example.banhangapi.api.repository;

import com.example.banhangapi.api.entity.UserHistoryOfvoucherUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserHistoryOfvoucherUsageRepository extends JpaRepository<UserHistoryOfvoucherUsage,String>, JpaSpecificationExecutor<UserHistoryOfvoucherUsage> {
    @Query(value = "SELECT COUNT(*) FROM user_history_ofvoucher_usage WHERE YEAR(create_time) = YEAR(CURDATE()) AND MONTH(create_time) = MONTH(CURDATE())", nativeQuery = true)
    Integer countRecordsCreatedInCurrentMonth();

    @Query(value = """
        SELECT count(*) FROM orders o
        JOIN user_history_ofvoucher_usage uhou ON (SELECT v.ID FROM voucher v WHERE v.voucher_code  = o.voucher_code )  = uhou.voucher_id
        WHERE uhou.create_by = :createBy and o.voucher_code  = :voucherCode
    """, nativeQuery = true)
    Integer countSlotUseVoucherByUser(@Param("createBy") String userId,@Param("voucherCode") String voucherCode);
}
