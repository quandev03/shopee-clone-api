package com.example.banhangapi.api.entity;

import com.example.banhangapi.api.globalEnum.AddressLever;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "address")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String nameAddress;

    @Nullable
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    Address beforeLevel;

    @Column()
    AddressLever addressLevel;

    @CreationTimestamp()
    LocalDateTime createdAt;

    @UpdateTimestamp()
    LocalDateTime updatedAt;

}
