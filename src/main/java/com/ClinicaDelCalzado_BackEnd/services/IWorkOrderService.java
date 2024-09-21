package com.ClinicaDelCalzado_BackEnd.services;

import com.ClinicaDelCalzado_BackEnd.dtos.request.WorkOrderDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.response.OrderByIdNumberDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.response.OrderListDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.response.WorkOrderDTOResponse;

public interface IWorkOrderService {

    WorkOrderDTOResponse createWorkOrder(WorkOrderDTORequest workOrderDTORequest);
    OrderByIdNumberDTOResponse getWorkOrderByOrderNumber(String orderNumber);
    OrderListDTOResponse getWorkOrderList(String orderStatus, String orderNumber, Long identification, String name, String phone, String attendedBy);
}
