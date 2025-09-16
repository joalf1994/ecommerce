package com.escuelajavag4.notification_service.mapper;

import com.escuelajavag4.notification_service.model.dto.response.NotificationDto;
import com.escuelajavag4.notification_service.model.entity.NotificationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationDto toDto(NotificationEntity notificationEntity);

    NotificationEntity toEntity(NotificationDto notificationDto);
}
