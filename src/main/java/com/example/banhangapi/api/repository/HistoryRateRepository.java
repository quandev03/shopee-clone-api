package com.example.banhangapi.api.repository;

import com.example.banhangapi.api.entity.HistoryRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HistoryRateRepository extends JpaRepository<HistoryRate, String> {

    @Query(value = """
        select sum(hr.rate)/COUNT(hr.id) as rate from history_rate hr
        where hr.product = :productId
    """, nativeQuery = true)
    Float getRateByProductId(String productId);

    @Query(value = """
        select count(*) from history_rate hr
        where hr.order_id  = :orderId
        and hr.created_by_id = :createBy
    """, nativeQuery = true)
    Integer checkRateOrder(String orderId, String createBy);
}
