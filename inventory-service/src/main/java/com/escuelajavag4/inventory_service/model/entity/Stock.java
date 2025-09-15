package com.escuelajavag4.inventory_service.model.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stock")
@Builder
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockId;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long warehouseId;

    @Column(nullable = false)
    @Builder.Default
    private int available = 0;

    @Column(nullable = false)
    private int reserved;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private Date  createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Date  updatedAt;

}
