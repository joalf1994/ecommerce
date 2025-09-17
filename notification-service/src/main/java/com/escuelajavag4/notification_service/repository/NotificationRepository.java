package com.escuelajavag4.notification_service.repository;

import com.escuelajavag4.notification_service.model.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    Optional<NotificationEntity> findFirstByOrderIdOrderBySentAtDesc(String orderId);
}
