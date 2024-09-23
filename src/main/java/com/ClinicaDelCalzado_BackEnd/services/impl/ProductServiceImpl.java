package com.ClinicaDelCalzado_BackEnd.services.impl;

import com.ClinicaDelCalzado_BackEnd.dtos.enums.ServicesStatusEnum;
import com.ClinicaDelCalzado_BackEnd.dtos.request.WorkOrderDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.workOrders.ServicesDTO;
import com.ClinicaDelCalzado_BackEnd.entity.ServicesEnt;
import com.ClinicaDelCalzado_BackEnd.entity.WorkOrder;
import com.ClinicaDelCalzado_BackEnd.repository.workOrders.IServicesRepository;
import com.ClinicaDelCalzado_BackEnd.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements IProductService {

    private final IServicesRepository servicesRepository;

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
                        .id(serv.getIdService())
                        .name(serv.getService())
                        .price(serv.getUnitValue().longValue())
                        .serviceStatus(ServicesStatusEnum.getValue(serv.getServiceStatus()))
                        .build())
                .toList();
    }

    @Override
    public List<ServicesDTO> saveServicesWorkOrder(WorkOrderDTORequest workOrderDTORequest, WorkOrder orderNumber) {

        return workOrderDTORequest.getServices().stream()
                .map(serviceDTO -> {
                    ServicesEnt service = ServicesEnt.builder()
                            .idOrderSer(orderNumber)
                            .service(serviceDTO.getName())
                            .unitValue(serviceDTO.getPrice().doubleValue())
                            .serviceStatus(ServicesStatusEnum.RECEIVED.getKeyName())
                            .build();
                    save(service);
                    return new ServicesDTO(service.getIdService(), service.getService(), service.getUnitValue().longValue(), service.getServiceStatus());
                }).collect(Collectors.toList());
    }
}
