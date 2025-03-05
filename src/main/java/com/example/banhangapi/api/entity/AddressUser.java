package com.example.banhangapi.api.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
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

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)

    Address detailAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @CreatedBy
    private User createdBy;

    private boolean isDefault = false;

    @NonNull
    private String numberPhone;


}
