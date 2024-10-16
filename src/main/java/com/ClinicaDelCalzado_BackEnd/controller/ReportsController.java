package com.ClinicaDelCalzado_BackEnd.controller;

import com.ClinicaDelCalzado_BackEnd.dtos.enums.OrderStatusEnum;
import com.ClinicaDelCalzado_BackEnd.dtos.response.DetailedReportDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.response.GeneralReportDTOResponse;
import com.ClinicaDelCalzado_BackEnd.services.IReportService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.ClinicaDelCalzado_BackEnd.util.Constants.BEFORE_DATE;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportsController {

    @Autowired
    private IReportService reportService;

    @GetMapping("/detailed")
    public ResponseEntity<DetailedReportDTOResponse> generateDetailedReport(@RequestParam(value = "order_status", required = false) List<String> orderStatus,
                                                                            @RequestParam(value = "start_date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime startDate,
                                                                            @RequestParam(value = "end_date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime endDate,
                                                                            Authentication authentication) {

        List<String> defaultStatus = Arrays.asList(OrderStatusEnum.VALID.getKeyName(), OrderStatusEnum.FINISHED.getKeyName());
        List<String> orderStatusList;

        if (ObjectUtils.isEmpty(orderStatus)) {
            orderStatusList = defaultStatus;
        } else {
            orderStatusList = orderStatus.stream()
                    .map(OrderStatusEnum::getName)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        if (ObjectUtils.isEmpty(startDate) || ObjectUtils.isEmpty(endDate)) {
            startDate = LocalDateTime.now().minusMonths(BEFORE_DATE);
            endDate = LocalDateTime.now();
        }

        DetailedReportDTOResponse detailedReportDTOResponse = reportService.getWorkOrderList(orderStatusList, startDate, endDate);
        return new ResponseEntity<>(detailedReportDTOResponse, HttpStatus.OK);
    }

    @GetMapping("/general")
    public ResponseEntity<GeneralReportDTOResponse> generateGeneralReport(@RequestParam(value = "order_status", required = false) List<String> orderStatus,
                                                                          @RequestParam(value = "start_date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime startDate,
                                                                          @RequestParam(value = "end_date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime endDate,
                                                                          Authentication authentication) {

        List<String> defaultStatus = Arrays.asList(OrderStatusEnum.VALID.getKeyName(), OrderStatusEnum.FINISHED.getKeyName());
        List<String> orderStatusList;

        if (ObjectUtils.isEmpty(orderStatus)) {
            orderStatusList = defaultStatus;
        } else {
            orderStatusList = orderStatus.stream()
                    .map(OrderStatusEnum::getName)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        if (ObjectUtils.isEmpty(startDate) || ObjectUtils.isEmpty(endDate)) {
            startDate = LocalDateTime.now().minusMonths(BEFORE_DATE);
            endDate = LocalDateTime.now();
        }

        GeneralReportDTOResponse generalReportDTOResponse = reportService.getWorkOrderListGeneral(orderStatusList, startDate, endDate);
        return new ResponseEntity<>(generalReportDTOResponse, HttpStatus.OK);
    }

}
