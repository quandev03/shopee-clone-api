package com.example.banhangapi.api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.apachecommons.CommonsLog;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Image implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String nameImage;

    @CreationTimestamp()
    LocalDateTime createTime;


    @UpdateTimestamp()
    LocalDateTime updateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createBy", nullable = true)
    @CreatedBy()
    User createdBy;

    @ManyToOne
    ProductEntity product;

    @Column(columnDefinition = "Text")
    String pathImage;

    @Column(columnDefinition = "Text")
    String description;

    @Column()
    boolean defaultImage;
}
