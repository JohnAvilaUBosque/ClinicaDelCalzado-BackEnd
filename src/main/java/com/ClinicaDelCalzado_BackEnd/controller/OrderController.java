package com.ClinicaDelCalzado_BackEnd.controller;

import com.ClinicaDelCalzado_BackEnd.dtos.enums.OrderStatusEnum;
import com.ClinicaDelCalzado_BackEnd.dtos.request.WorkOrderDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.response.OrderByIdNumberDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.response.OrderListDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.response.WorkOrderDTOResponse;
import com.ClinicaDelCalzado_BackEnd.services.IWorkOrderService;
import org.apache.commons.lang3.ObjectUtils;
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
    public ResponseEntity<OrderListDTOResponse> getWorkOrderList(@RequestParam(value = "order_status", required = false) String orderStatus,
                                                                 @RequestParam(value = "order_number", required = false) String orderNumber,
                                                                 @RequestParam(value = "identification", required = false) Long identification,
                                                                 @RequestParam(value = "name", required = false) String name,
                                                                 @RequestParam(value = "phone", required = false) String phone,
                                                                 @RequestParam(value = "attended_by", required = false) String attendedBy,
                                                                 Authentication authentication) {

        orderStatus = ObjectUtils.isEmpty(orderStatus) ? OrderStatusEnum.VALID.getKeyName() : OrderStatusEnum.getName(orderStatus);

        OrderListDTOResponse orderListDTOResponse = workOrderService.getWorkOrderList(orderStatus, orderNumber, identification, name, phone, attendedBy);
        return new ResponseEntity<>(orderListDTOResponse, HttpStatus.OK);
    }
}
