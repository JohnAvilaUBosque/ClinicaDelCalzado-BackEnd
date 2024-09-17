package com.ClinicaDelCalzado_BackEnd.services;

import com.ClinicaDelCalzado_BackEnd.dtos.workOrders.ServicesDTO;
import com.ClinicaDelCalzado_BackEnd.entity.ServicesEnt;

import java.util.List;

public interface IProductService {
    ServicesEnt save(ServicesEnt services);
    List<ServicesDTO> getServicesOrder(String orderNumber);
}