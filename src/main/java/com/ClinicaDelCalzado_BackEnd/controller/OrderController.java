package com.ClinicaDelCalzado_BackEnd.controller;

import com.ClinicaDelCalzado_BackEnd.dtos.request.WorkOrderDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.response.OrderByIdNumberDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.response.OrderListDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.response.WorkOrderDTOResponse;
import com.ClinicaDelCalzado_BackEnd.services.IWorkOrderService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/work-orders")
public class OrderController {

    @Autowired
    private IWorkOrderService workOrderService;

    @PostMapping("/created")
    public ResponseEntity<WorkOrderDTOResponse> createWorkOrder(@RequestBody WorkOrderDTORequest workOrderDTO) {

        WorkOrderDTOResponse responseDTO = workOrderService.createWorkOrder(workOrderDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{orderNumber}")
    public ResponseEntity<OrderByIdNumberDTOResponse> getWorkOrderByOrderNumber(@PathVariable String orderNumber, Authentication authentication) {

        OrderByIdNumberDTOResponse orderByIdNumberDTOResponse = workOrderService.getWorkOrderByOrderNumber(orderNumber);
        return new ResponseEntity<>(orderByIdNumberDTOResponse, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<OrderListDTOResponse> getWorkOrderList(@PathParam("order_number") String orderNumber, @PathParam("identification") Long identification,
                                                                 @PathParam("name") String name, @PathParam("phone") String phone,
                                                                 @PathParam("attended_by") String attendedBy, Authentication authentication) {

        OrderListDTOResponse orderListDTOResponse = workOrderService.getWorkOrderList(orderNumber, identification, name, phone, attendedBy);
        return new ResponseEntity<>(orderListDTOResponse, HttpStatus.OK);
    }
}
