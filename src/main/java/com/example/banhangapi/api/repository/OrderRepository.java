package com.example.banhangapi.api.repository;

import com.example.banhangapi.api.dto.Revenue7DayRecent;
import com.example.banhangapi.api.entity.Order;
import com.example.banhangapi.api.entity.User;
import com.example.banhangapi.api.globalEnum.StatusOrder;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {
    List<Order> findAllByOrderByIdDesc();

    List<Order> findAllByStatusOrderAndCreatedBy(Integer statusOrder, User createdBy);
    List<Order> findAllByCreatedBy(User createdBy);


    @Query("""
        SELECT SUM(o.quantity * p.price)
        FROM orders o
        JOIN o.productEntity p
        WHERE o.statusOrder = 3
    """)
    Double getTotalAmountInDash();
    @Query("""
        SELECT COUNT(*) 
        FROM orders o
    """)
    Integer getTotalOrders();

    @Query(value = "SELECT * FROM orders ORDER BY create_time DESC LIMIT 5", nativeQuery = true)
    List<Order> findTop5RecentOrders();



    @Query("SELECT " +
           "SUM(o.quantity * p.price), " +
           "function('DATE_FORMAT', o.createTime, '%d/%m/%Y') " +
           "FROM orders o " +
           "JOIN o.productEntity p " +
           "WHERE o.statusOrder = 0 AND o.createTime >= :startDate " +
           "GROUP BY function('DATE_FORMAT', o.createTime, '%d/%m/%Y') " +
           "ORDER BY function('DATE_FORMAT', o.createTime, '%d/%m/%Y') DESC")
    List<Object[]> getDailyRevenueForLast7Days(@Param("startDate") LocalDateTime startDate);

    @Query("""
        SELECT o 
        FROM orders o 
        WHERE (:startDate IS NULL OR o.createTime >= :startDate) 
        AND (:endDate IS NULL OR o.createTime <= :endDate) 
        ORDER BY o.createTime DESC
    """)
    Page<Order> findOrdersByCreateTimeRange(@Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate);

    @Transactional
    @Modifying
    @Query("""
        UPDATE orders o SET o.statusOrder = :orderStatus
            WHERE o.id = :orderId
    """)
    void updateStatus(@Param("orderId") String orderId, @Param("orderStatus") Integer orderStatusId);

    @Query(value = """
        SELECT (count(*) + 1) FROM orders 
    """, nativeQuery = true)
    Integer getOrdersCount();
}
