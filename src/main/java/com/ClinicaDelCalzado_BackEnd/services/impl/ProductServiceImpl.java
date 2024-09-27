package com.ClinicaDelCalzado_BackEnd.services.impl;

import com.ClinicaDelCalzado_BackEnd.dtos.enums.ServicesStatusEnum;
import com.ClinicaDelCalzado_BackEnd.dtos.request.WorkOrderDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.workOrders.ServicesDTO;
import com.ClinicaDelCalzado_BackEnd.entity.ServicesEntity;
import com.ClinicaDelCalzado_BackEnd.entity.WorkOrder;
import com.ClinicaDelCalzado_BackEnd.repository.workOrders.IServicesRepository;
import com.ClinicaDelCalzado_BackEnd.services.IProductService;
import org.apache.commons.lang3.ObjectUtils;
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
    public ServicesEntity save(ServicesEntity services) {
        return servicesRepository.save(services);
    }

    @Override
    public List<ServicesDTO> getServicesOrder(String orderNumber) {
        List<ServicesEntity> servicesList = servicesRepository.findByWorkOrder(orderNumber);

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
                    ServicesEntity service = ServicesEntity.builder()
                            .idOrderSer(orderNumber)
                            .service(serviceDTO.getName())
                            .unitValue(serviceDTO.getPrice().doubleValue())
                            .serviceStatus(ServicesStatusEnum.RECEIVED.getKeyName())
                            .hasPendingUnitValue(!ObjectUtils.isNotEmpty(serviceDTO.getPrice()) || serviceDTO.getPrice() <= 0)
                            .build();
                    save(service);
                    return new ServicesDTO(service.getIdService(), service.getService(),
                            service.getUnitValue().longValue(), service.getServiceStatus(),
                            service.getHasPendingUnitValue());
                }).collect(Collectors.toList());
    }
}
