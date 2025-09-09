package com.escuelajavag4.inventory_service.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long stockId;

    @Column
    private Long productId;

    @Column
    private Long warehouseId;

    @Column
    private int available;

    @Column
    private int reserved;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Date  createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Date  updatedAt;

}
