package com.ClinicaDelCalzado_BackEnd.dtos.Response;

import com.ClinicaDelCalzado_BackEnd.dtos.workOrders.WorkOrderDTORes;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WorkOrderDTOResponse {

    private String message;
    private WorkOrderDTORes order;
}
