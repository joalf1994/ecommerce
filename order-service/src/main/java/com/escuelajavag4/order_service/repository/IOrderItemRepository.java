package com.escuelajavag4.order_service.repository;

import com.escuelajavag4.order_service.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findOrderItemsByProductId(String productId);
}
