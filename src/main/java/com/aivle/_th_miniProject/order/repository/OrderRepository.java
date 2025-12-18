package com.aivle._th_miniProject.order.repository;

import com.aivle._th_miniProject.order.entity.Order;
import com.aivle._th_miniProject.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
