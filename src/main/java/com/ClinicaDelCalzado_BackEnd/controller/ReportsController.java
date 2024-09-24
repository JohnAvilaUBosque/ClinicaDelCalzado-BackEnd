package com.ClinicaDelCalzado_BackEnd.controller;

import com.ClinicaDelCalzado_BackEnd.dtos.enums.OrderStatusEnum;
import com.ClinicaDelCalzado_BackEnd.dtos.response.DetailedReportDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.response.OrderListDTOResponse;
import com.ClinicaDelCalzado_BackEnd.services.IReportService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportsController {

    @Autowired
    private IReportService reportService;

    @GetMapping("/detailed")
    public ResponseEntity<DetailedReportDTOResponse> generateDetailedReport(@RequestParam(value = "order_status", required = false) String orderStatus,
                                                                      @RequestParam(value = "order_number", required = false) String orderNumber,
                                                                      @RequestParam(value = "identification", required = false) Long identification,
                                                                      @RequestParam(value = "name", required = false) String name,
                                                                      @RequestParam(value = "phone", required = false) String phone,
                                                                      @RequestParam(value = "attended_by", required = false) String attendedBy,
                                                                      Authentication authentication) {

        orderStatus = ObjectUtils.isEmpty(orderStatus) ? OrderStatusEnum.VALID.getKeyName() : OrderStatusEnum.getName(orderStatus);

        //OrderListDTOResponse orderListDTOResponse = workOrderService.getWorkOrderList(orderStatus, orderNumber, identification, name, phone, attendedBy);
        return null; // new ResponseEntity<>(orderListDTOResponse, HttpStatus.OK);
    }
}
