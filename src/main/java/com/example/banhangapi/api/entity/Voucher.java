package com.example.banhangapi.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "voucher")
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Min(0)
    @Max(1)
    @Column()
    float discount;

    @CreationTimestamp()
    LocalDateTime createdAt;

    @UpdateTimestamp()
    LocalDateTime updatedAt;

    @Column(nullable = false)
    String voucherCode;

    @Column(columnDefinition = "Text")
    String description;

    @Min(0)
    @Column()
    int limitSlot;

    @Column()
    String expirationDate;

    @Column()
    String startDate;

    Boolean limitedUsage;

    int slotUsageForUser;

}
