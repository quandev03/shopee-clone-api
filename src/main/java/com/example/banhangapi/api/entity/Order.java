package com.example.banhangapi.api.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import com.example.banhangapi.api.globalEnum.StatusOrder;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Entity(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @CreationTimestamp
    LocalDateTime createTime;

    @UpdateTimestamp
    LocalDateTime updateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createBy", nullable = true)
    @CreatedBy()
    User createdBy;

    StatusOrder statusOrder = StatusOrder.ORDER_WAITING_FOR_CONFIRMATION;

    Long totalAmountOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    Voucher voucherApplyForOrder;



    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    List<OrderDetails> orderDetails = new ArrayList<>();

    public void  addOrderDetailForOrder(OrderDetails newOrder){
        orderDetails.add(newOrder);
    };

    public void removeOrderDetail(OrderDetails orderDetails){
        this.orderDetails.remove(orderDetails);
    }


}
