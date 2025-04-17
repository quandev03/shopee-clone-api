package com.example.banhangapi.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ProductEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @CreationTimestamp()
    LocalDateTime createTime;

    @UpdateTimestamp()
    LocalDateTime updateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_by", nullable = true)
    @CreatedBy
    User createBy;

    @Column(nullable = false, unique = true, name = "nameProduct")
    private String nameProduct;

    private String description;
    private Double price;
    private long quantity;
    @Min(value = 0)
    private long soldQuantity;

    @ColumnDefault("0")
    private Long viewedQuantity = 0L;

    private String image;

    @ColumnDefault("0")
    private Integer rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoty_id")
    @JsonIgnore
    private Category category;

}
