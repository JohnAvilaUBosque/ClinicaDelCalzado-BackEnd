package com.ClinicaDelCalzado_BackEnd.dtos.response;

import com.ClinicaDelCalzado_BackEnd.dtos.userAdmin.AdminDTO;
import com.ClinicaDelCalzado_BackEnd.dtos.workOrders.ClientDTO;
import com.ClinicaDelCalzado_BackEnd.dtos.workOrders.CompanyDTO;
import com.ClinicaDelCalzado_BackEnd.dtos.workOrders.ServicesDTO;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderByIdNumberDTOResponse {

    private String orderNumber;
    private CompanyDTO company;
    private AdminDTO attendedBy;
    private LocalDateTime createDate;
    private String orderStatus;
    private LocalDate deliveryDate;
    private ClientDTO client;
    private List<ServicesDTO> services;
    private String comment;
    private Double downPayment;
    private Double totalValue;
    private Double balance;
    private String paymentStatus;
}
