package com.ClinicaDelCalzado_BackEnd.dtos.workOrders;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class WorkOrderDTO {

    private CompanyDTO company;
    private Integer attendedById;
    private String orderNumber;
    private String orderStatus;
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate deliveryDate;
    private ClientDTO client;
    private List<ServicesDTO> services;
    private String generalComment;
    private Double downPayment;
    private String paymentStatus;
}
