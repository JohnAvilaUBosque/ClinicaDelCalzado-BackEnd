package com.ClinicaDelCalzado_BackEnd.dtos.response;

import com.ClinicaDelCalzado_BackEnd.dtos.reports.DetailedReportDTO;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DetailedReportDTOResponse {

    private List<DetailedReportDTO> orders;
}
