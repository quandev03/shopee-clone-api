package com.example.banhangapi.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "history_rate")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryRate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order orderId;

    @JoinColumn(name = "product", referencedColumnName = "id")
    @ManyToOne
    private ProductEntity product;

    @CreatedBy
    @ManyToOne
    private User createdBy;

    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "rate")
    private Integer rate;
}
