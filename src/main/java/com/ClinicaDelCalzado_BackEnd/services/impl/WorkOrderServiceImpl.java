package com.ClinicaDelCalzado_BackEnd.services.impl;

import com.ClinicaDelCalzado_BackEnd.dtos.Request.WorkOrderDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.Response.WorkOrderDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.workOrders.*;
import com.ClinicaDelCalzado_BackEnd.entity.*;
import com.ClinicaDelCalzado_BackEnd.repository.userAdmin.IAdministratorRepository;
import com.ClinicaDelCalzado_BackEnd.repository.workOrders.IClientRepository;
import com.ClinicaDelCalzado_BackEnd.repository.workOrders.ICompanyRepository;
import com.ClinicaDelCalzado_BackEnd.repository.workOrders.IServicesRepository;
import com.ClinicaDelCalzado_BackEnd.repository.workOrders.IWorkOrderRepository;
import com.ClinicaDelCalzado_BackEnd.services.ICompanyService;
import com.ClinicaDelCalzado_BackEnd.services.IWorkOrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WorkOrderServiceImpl implements IWorkOrderService {

    private IWorkOrderRepository workOrderRepository;

    private ICompanyService companyService;

    private IClientRepository clientRepository;

    private IServicesRepository serviceRepository;

    private IAdministratorRepository administratorRepository;

    //private Optional<Company> company;
    private final List<OrderErrorDTO> orderErrors = new ArrayList<>();

    @Autowired
    public WorkOrderServiceImpl(IWorkOrderRepository workOrderRepository, ICompanyService companyService,
                                IClientRepository clientRepository, IServicesRepository serviceRepository,
                                IAdministratorRepository administratorRepository) {
        this.workOrderRepository = workOrderRepository;
        this.companyService = companyService;
        this.clientRepository = clientRepository;
        this.serviceRepository = serviceRepository;
        this.administratorRepository = administratorRepository;
    }

    @Override
    public WorkOrderDTOResponse createWorkOrder(WorkOrderDTORequest workOrderDTORequest) {

        validateRequest(workOrderDTORequest);

        // Buscar o crear la empresa
        Company company = companyService.findCompanyByNit(workOrderDTORequest.getCompany().getNit())
                .orElseGet(() -> companyService.save(
                        Company.builder()
                                .nit(workOrderDTORequest.getCompany().getNit())
                                .name(workOrderDTORequest.getCompany().getName())
                                .address(workOrderDTORequest.getCompany().getAddress())
                                .phones(String.join(",", workOrderDTORequest.getCompany().getPhones()))
                                .build())
                );
/*
        // Buscar o crear el cliente utilizando orElseGet con lambda
        Client client = clientRepository.findByIdentification(workOrderDTORequest.getClient().getIdentification())
                .orElseGet(() -> clientRepository.save(
                        new Client(
                                workOrderDTORequest.getClient().getName(),
                                workOrderDTORequest.getClient().getCellphone()
                        )
                ));

        // Buscar el administrador y lanzar excepción si no se encuentra
        Administrator attendedBy = administratorRepository.findById(workOrderDTORequest.getAttendedById())
                .orElseThrow(() -> new RuntimeException("Administrator not found"));

        // Crear la orden de trabajo
        WorkOrder workOrder = workOrderRepository.save(
                new WorkOrder(
                        workOrderDTORequest.getOrderNumber(),
                        LocalDate.parse(workOrderDTORequest.getCreateDate().toString()),
                        LocalDate.parse(workOrderDTORequest.getDeliveryDate().toString()),
                        workOrderDTORequest.getOrderStatus(),
                        workOrderDTORequest.getPaymentStatus(),
                        attendedBy,
                        client,
                        company,
                        workOrderDTORequest.getDownPayment(),
                        null,  // Total value (calculated next)
                        null,  // Balance (calculated next)
                        null   // General comment (if needed)
                )
        );

        // Calcular el total y saldo
        double totalValue = workOrderDTORequest.getServices().stream()
                .mapToDouble(ServicesDTO::getPrice)
                .sum();
        workOrder.setTotalValue(totalValue);
        workOrder.setBalance(totalValue - workOrderDTORequest.getDownPayment());

        // Guardar los servicios y mapear a DTOs
        List<ServicesDTO> services = workOrderDTORequest.getServices().stream()
                .map(serviceDTO -> {
                    ServicesEnt service = new ServicesEnt(
                            serviceDTO.getName(),
                            serviceDTO.getPrice(),
                            workOrderDTORequest.getOrderStatus(),
                            workOrder
                    );
                    serviceRepository.save(service);
                    return new ServicesDTO(service.getService(), service.getUnitValue(), service.getServiceStatus());
                })
                .collect(Collectors.toList());

        // Crear el DTO de respuesta
        ClientDTO clientResponseDTO = new ClientDTO(client.getIdClient(), client.getClientName(), client.getCliPhoneNumber());
        WorkOrderDTORes orderResponseDTO = new WorkOrderDTORes(
                company,
                workOrder.getOrderNumber(),
                attendedBy.getAdminName(),
                workOrder.getCreationDate(),
                workOrder.getOrderStatus(),
                workOrder.getDeliveryDate(),
                clientResponseDTO,
                services,
                workOrder.getCommentsList().toString(), // Si hay comentarios
                workOrder.getDeposit(),
                workOrder.getTotalValue(),
                workOrder.getBalance(),
                workOrder.getPaymentStatus()
        );

        return new WorkOrderDTOResponse("Orden de trabajo creada exitosamente!", orderResponseDTO);*/
        return null;
    }

    private void validateRequest(WorkOrderDTORequest workOrderDTORequest) {
        String nit = workOrderDTORequest.getCompany().getNit();

        //Validar si la compañia esta registrada
        if (nit.isEmpty()) {
            throw new NotFoundException(String.format("La compañía con nit %s no esta registrada!!", nit));
        }
    }
}
