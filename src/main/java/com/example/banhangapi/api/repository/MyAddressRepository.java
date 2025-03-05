package com.example.banhangapi.api.repository;

import com.example.banhangapi.api.entity.AddressUser;
import com.example.banhangapi.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MyAddressRepository extends JpaRepository<AddressUser, String>, JpaSpecificationExecutor<AddressUser> {
    List<AddressUser> findByCreatedBy(User user);
}
