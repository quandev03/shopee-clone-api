package com.example.banhangapi.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @CreationTimestamp()
    LocalDateTime createdAt;

    @UpdateTimestamp()
    LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    Address provincialAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    Address districtAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    Address commercalAddress;

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    Address specailAddress;

    String detailAddress;

    String fullName;
    String phone;


    @ManyToOne(fetch = FetchType.LAZY)
    @CreatedBy()
    @JsonIgnore
    private User createdBy;

    private boolean defaultAddress = false;

}
