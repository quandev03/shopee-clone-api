package com.example.banhangapi.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    private float price;
    private long quantity;
    @Min(value = 0)
    private long soldQuantity;

    @ColumnDefault("0")
    private Long viewedQuantity = 0L;
//
//    @OneToMany(
//            mappedBy = "productEntity",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true
//    )
//    private List<Comment> commentEntities = new ArrayList<>();
//
//    public void addComment(Comment commentEntity) {
//        commentEntities.add(commentEntity);
//    }
//
//    public void removeComment(Comment commentEntity) {
//        commentEntities.remove(commentEntity);
//    }

}
