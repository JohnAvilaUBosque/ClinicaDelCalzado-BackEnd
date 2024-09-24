package com.ClinicaDelCalzado_BackEnd.dtos.detailedReport;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DetailedReportDTO {

    private LocalDateTime creationDate;
    private String orderNumber;
    private Double totalServicesValue;
    private Double totalDeposits;
    private Double totalBalance;
    private Integer servicesReceived;
    private Integer servicesCompleted;
    private Integer servicesDispatched;
}
