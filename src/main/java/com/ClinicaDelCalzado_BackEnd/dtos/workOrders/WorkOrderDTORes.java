package com.ClinicaDelCalzado_BackEnd.dtos.workOrders;

import com.ClinicaDelCalzado_BackEnd.entity.Company;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WorkOrderDTORes {

    private Company company;
    /*private String orderNumber;
    private String attendedBy;
    private LocalDate createDate;
    private String orderStatus;
    private LocalDate deliveryDate;
    private ClientDTO client;
    private List<ServicesDTO> services;
    private String getGeneralComment;
    private Double downPayment;
    private Double totalValue;
    private Double balance;
    private String paymentStatus;*/
}
