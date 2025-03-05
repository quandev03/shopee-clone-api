package com.example.banhangapi.api.repository;

import com.example.banhangapi.api.entity.Address;
import com.example.banhangapi.api.globalEnum.AddressLever;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, String>, JpaSpecificationExecutor<Address> {
    Optional<Address> findById(String id);

    List<Address> findAllByAddressLevel(AddressLever addressLever);

    List<Address> findAllByBeforeLevel(Address beforeLevel);
}
