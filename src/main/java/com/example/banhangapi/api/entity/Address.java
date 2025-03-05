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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "address")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false)
    String nameAddress;

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    Address beforeLevel;

    @Column()
    AddressLever addressLevel;

    @OneToMany(
            mappedBy = "beforeLevel",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    List<Address> nextLevel = new ArrayList<>();

    public void addNextLeverAddress(Address address) {
        nextLevel.add(address);
    }

    public void removeNextLeverAddress(Address address) {
        nextLevel.remove(address);
    };

    @CreationTimestamp()
    LocalDateTime createdAt;

    @UpdateTimestamp()
    LocalDateTime updatedAt;

}
