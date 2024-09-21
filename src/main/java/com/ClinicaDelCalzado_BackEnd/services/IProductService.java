package com.ClinicaDelCalzado_BackEnd.services;

import com.ClinicaDelCalzado_BackEnd.dtos.request.WorkOrderDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.workOrders.ServicesDTO;
import com.ClinicaDelCalzado_BackEnd.entity.ServicesEnt;
import com.ClinicaDelCalzado_BackEnd.entity.WorkOrder;

import java.util.List;

public interface IProductService {
    ServicesEnt save(ServicesEnt services);
    List<ServicesDTO> getServicesOrder(String orderNumber);
    List<ServicesDTO> saveServicesWorkOrder(WorkOrderDTORequest workOrderDTORequest, WorkOrder orderNumber);
}
