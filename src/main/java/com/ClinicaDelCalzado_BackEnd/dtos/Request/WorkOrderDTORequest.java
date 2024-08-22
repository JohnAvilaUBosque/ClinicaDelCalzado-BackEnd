package com.ClinicaDelCalzado_BackEnd.dtos.Request;

import com.ClinicaDelCalzado_BackEnd.dtos.workOrders.WorkOrderDTO;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WorkOrderDTORequest {

    private @Valid WorkOrderDTO workOrder;
}
