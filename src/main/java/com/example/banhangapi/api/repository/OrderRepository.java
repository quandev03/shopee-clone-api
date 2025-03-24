package com.example.banhangapi.api.repository;

import com.example.banhangapi.api.entity.Order;
import com.example.banhangapi.api.entity.User;
import com.example.banhangapi.api.globalEnum.StatusOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {
    List<Order> findAllByOrderByIdDesc();

    List<Order> findAllByStatusOrderAndCreatedBy(StatusOrder statusOrder, User createdBy);
    List<Order> findAllByCreatedBy(User createdBy);
}
