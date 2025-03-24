package com.example.banhangapi.api.repository;

import com.example.banhangapi.api.dto.AddressDTO;
import com.example.banhangapi.api.entity.Address;
import com.example.banhangapi.api.globalEnum.AddressLever;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, String>, JpaSpecificationExecutor<Address> {
    Optional<Address> findById(String id);

    List<Address> findAllByAddressLevel(AddressLever addressLever);


    @Modifying
    @Query("SELECT a.id, a.nameAddress, a.beforeLevel  FROM Address a WHERE a.addressLevel <> 0" )
    List<Tuple> findAllNotProvince();



    List<Address> findAllByBeforeLevel(Address address);


    List<Address> findAllByBeforeLevelNotNull();
}
