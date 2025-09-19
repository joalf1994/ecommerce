package com.escuelajavag4.notification_service.service;
import com.escuelajavag4.notification_service.mapper.NotificationMapper;
import com.escuelajavag4.notification_service.model.dto.request.PaymentCompletedEvent;
import com.escuelajavag4.notification_service.model.dto.response.NotificationDto;
import com.escuelajavag4.notification_service.model.entity.NotificationEntity;
import com.escuelajavag4.notification_service.repository.NotificationRepository;
import com.escuelajavag4.notification_service.service.impl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {
    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationMapper notificationMapper;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private PaymentCompletedEvent event;

    @BeforeEach
    void setUp() {
        event = new PaymentCompletedEvent();
        event.setPaymentId(1L);
        event.setOrderId(123L);
        event.setAmount(BigDecimal.valueOf(100.0));
        event.setStatus("COMPLETED");
        event.setEmail("sandrogopher@gmail.com");

        // Spy sobre notificationService para no ejecutar sendEmail real
        notificationService = Mockito.spy(notificationService);

        // Marca el stub como "lenient" para que no falle si no se usa
        lenient().doNothing().when(notificationService).sendEmail(any(PaymentCompletedEvent.class));
    }

    @Test
    void processOrderConfirmedEvent_shouldSaveNotification() {
        notificationService.processOrderConfirmedEvent(event);

        ArgumentCaptor<NotificationEntity> captor = ArgumentCaptor.forClass(NotificationEntity.class);
        verify(notificationRepository).save(captor.capture());

        NotificationEntity saved = captor.getValue();
        assertThat(saved.getOrderId()).isEqualTo(123L);
        assertThat(saved.getStatus()).isEqualTo("COMPLETED");
        assertThat(saved.getChannel()).isEqualTo("EMAIL");
        assertThat(saved.getSentAt()).isNotNull();
    }

    @Test
    void getNotificationByOrderId_shouldReturnDto() {
        NotificationEntity notificationEntity = new NotificationEntity(1L, 123L, "SENT", "EMAIL", Instant.now());
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setOrderId(123L);
        notificationDto.setStatus("SENT");
        notificationDto.setChannel("EMAIL");
        notificationDto.setSentAt(notificationEntity.getSentAt());

        when(notificationRepository.findFirstByOrderIdOrderBySentAtDesc(123L)).thenReturn(Optional.of(notificationEntity));
        when(notificationMapper.toDto(notificationEntity)).thenReturn(notificationDto);

        NotificationDto result = notificationService.getNotificationByOrderId(123L);

        assertThat(result.getOrderId()).isEqualTo(123L);
        assertThat(result.getStatus()).isEqualTo("SENT");
        assertThat(result.getChannel()).isEqualTo("EMAIL");
    }

    @Test
    void getNotificationByOrderId_shouldThrowWhenNotFound() {
        when(notificationRepository.findFirstByOrderIdOrderBySentAtDesc(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> notificationService.getNotificationByOrderId(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Notificación no encontrada");
    }
}
