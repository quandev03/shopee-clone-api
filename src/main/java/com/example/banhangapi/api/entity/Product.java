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
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @CreationTimestamp
    protected LocalDateTime createTime;

    @UpdateTimestamp
    protected LocalDateTime updateTime;

    // Thiết lập mối quan hệ Many-to-One tới User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createBy", nullable = true)
    @CreatedBy
    protected User createBy;

    @Column(nullable = false, unique = true, name = "nameProduct")
    private String nameProduct;

    private String description;
    private float price;
    private long quantity;
    @Min(value = 0)
    private long soldQuantity;

    @ColumnDefault("0L")
    private Long viewedQuantity = 0L;

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Comment> comments = new ArrayList<>();

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
    }

}
