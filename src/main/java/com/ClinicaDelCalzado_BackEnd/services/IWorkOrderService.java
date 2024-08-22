package com.ClinicaDelCalzado_BackEnd.services;

import com.ClinicaDelCalzado_BackEnd.dtos.Request.WorkOrderDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.Response.WorkOrderDTOResponse;

public interface IWorkOrderService {

    WorkOrderDTOResponse createWorkOrder(WorkOrderDTORequest workOrderDTO);
}
