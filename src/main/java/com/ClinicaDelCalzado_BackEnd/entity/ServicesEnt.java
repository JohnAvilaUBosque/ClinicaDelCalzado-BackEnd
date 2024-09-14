package com.ClinicaDelCalzado_BackEnd.entity;

import com.ClinicaDelCalzado_BackEnd.dtos.enums.ServicesStatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "service")
public class ServicesEnt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_service", length = 20)
    private Integer idService;

    @ManyToOne
    @JoinColumn(name = "id_order")
    private WorkOrder idOrderSer;

    @Column(name = "service")
    private String service;

    @Column(name = "service_status", length = 10)
    private String serviceStatus;

    @Column(name = "unit_value")
    private Double unitValue;
}