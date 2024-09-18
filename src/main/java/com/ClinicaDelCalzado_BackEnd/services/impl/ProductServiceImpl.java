package com.ClinicaDelCalzado_BackEnd.services.impl;

import com.ClinicaDelCalzado_BackEnd.dtos.enums.ServicesStatusEnum;
import com.ClinicaDelCalzado_BackEnd.dtos.workOrders.ServicesDTO;
import com.ClinicaDelCalzado_BackEnd.entity.ServicesEnt;
import com.ClinicaDelCalzado_BackEnd.repository.workOrders.IServicesRepository;
import com.ClinicaDelCalzado_BackEnd.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    private IServicesRepository servicesRepository;

    @Autowired
    public ProductServiceImpl(IServicesRepository servicesRepository) {
        this.servicesRepository = servicesRepository;
    }

    @Override
    public ServicesEnt save(ServicesEnt services) {
        return servicesRepository.save(services);
    }

    @Override
    public List<ServicesDTO> getServicesOrder(String orderNumber) {
        List<ServicesEnt> servicesList = servicesRepository.findByWorkOrder(orderNumber);

        if (servicesList.isEmpty()) {
            throw new NotFoundException(String.format("La orden %s no esta registrada!!", orderNumber));
        }

        return servicesList.stream()
                .map(serv -> ServicesDTO.builder()
                        .name(serv.getService())
                        .price(serv.getUnitValue().longValue())
                        .serviceStatus(ServicesStatusEnum.getValue(serv.getServiceStatus()))
                        .build())
                .toList();
    }
}
