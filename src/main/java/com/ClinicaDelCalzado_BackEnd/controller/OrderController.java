package com.ClinicaDelCalzado_BackEnd.controller;

import com.ClinicaDelCalzado_BackEnd.dtos.Request.WorkOrderDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.Response.WorkOrderDTOResponse;
import com.ClinicaDelCalzado_BackEnd.services.IWorkOrderService;
import com.ClinicaDelCalzado_BackEnd.services.impl.WorkOrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/work-orders")
public class OrderController {

    @Autowired
    private IWorkOrderService iWorkOrderService;

    @PostMapping("/created")
    public ResponseEntity<WorkOrderDTOResponse> createWorkOrder(@RequestBody WorkOrderDTORequest workOrderDTO) {

        WorkOrderDTOResponse responseDTO = iWorkOrderService.createWorkOrder(workOrderDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
}
