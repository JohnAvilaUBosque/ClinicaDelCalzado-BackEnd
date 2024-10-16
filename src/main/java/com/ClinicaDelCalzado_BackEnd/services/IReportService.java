package com.ClinicaDelCalzado_BackEnd.services;

import com.ClinicaDelCalzado_BackEnd.dtos.response.DetailedReportDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.response.GeneralReportDTOResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface IReportService {
    DetailedReportDTOResponse getWorkOrderList(List<String> orderStatus, LocalDateTime startDate, LocalDateTime endDate);
    GeneralReportDTOResponse getWorkOrderListGeneral(List<String> orderStatus, LocalDateTime startDate, LocalDateTime endDate);
}
