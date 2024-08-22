package com.ClinicaDelCalzado_BackEnd.controller;

import com.ClinicaDelCalzado_BackEnd.dtos.Request.WorkOrderDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.Response.WorkOrderDTOResponse;
import com.ClinicaDelCalzado_BackEnd.services.impl.WorkOrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/work-orders")
public class OrderController {

    @Autowired
    private WorkOrderServiceImpl workOrderServiceImpl;

//    @PostMapping("/created")
//    public ResponseEntity<WorkOrderDTOResponse> createWorkOrder(@RequestBody WorkOrderDTORequest workOrderDTO) {
//
//        return ResponseEntity.status(201).body(workOrderServiceImpl.createWorkOrder(workOrderDTO));
//    }
}
