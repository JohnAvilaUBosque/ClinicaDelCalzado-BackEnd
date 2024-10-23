package com.ClinicaDelCalzado_BackEnd.dtos.reports;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime creationDate;
    private String orderNumber;
    private Long totalServicesValue;
    private Long totalDeposits;
    private Long totalBalance;
    private Long servicesReceived;
    private Long servicesCompleted;
    private Long servicesDispatched;
    private String orderStatus;
}
