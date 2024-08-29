package com.ClinicaDelCalzado_BackEnd.services.impl;

import com.ClinicaDelCalzado_BackEnd.dtos.Request.WorkOrderDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.Response.WorkOrderDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.workOrders.*;
import com.ClinicaDelCalzado_BackEnd.entity.*;
import com.ClinicaDelCalzado_BackEnd.repository.workOrders.IServicesRepository;
import com.ClinicaDelCalzado_BackEnd.repository.workOrders.IWorkOrderRepository;
import com.ClinicaDelCalzado_BackEnd.services.IAdminService;
import com.ClinicaDelCalzado_BackEnd.services.IClientService;
import com.ClinicaDelCalzado_BackEnd.services.ICompanyService;
import com.ClinicaDelCalzado_BackEnd.services.IWorkOrderService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;


@Service
public class WorkOrderServiceImpl implements IWorkOrderService {

    private final IWorkOrderRepository workOrderRepository;

    private final ICompanyService companyService;

    private final IClientService clientService;

    private final IServicesRepository serviceRepository;

    private final IAdminService adminService;

    //private Optional<Company> company;
    private final List<OrderErrorDTO> orderErrors = new ArrayList<>();

    @Autowired
    public WorkOrderServiceImpl(IWorkOrderRepository workOrderRepository, ICompanyService companyService,
                                IClientService clientService, IServicesRepository serviceRepository,
                                IAdminService adminService) {
        this.workOrderRepository = workOrderRepository;
        this.companyService = companyService;
        this.clientService = clientService;
        this.serviceRepository = serviceRepository;
        this.adminService = adminService;
    }

    @Override
    public WorkOrderDTOResponse createWorkOrder(WorkOrderDTORequest workOrderDTORequest) {

        validateRequest(workOrderDTORequest);

        Company company = companyService.findCompanyByNit(workOrderDTORequest.getCompany().getNit())
                .orElseGet(() -> companyService.save(
                        Company.builder()
                                .nit(workOrderDTORequest.getCompany().getNit())
                                .name(workOrderDTORequest.getCompany().getName())
                                .address(workOrderDTORequest.getCompany().getAddress())
                                .phones(String.join(",", workOrderDTORequest.getCompany().getPhones()))
                                .build())
                );

        Client client = clientService.findClientByIdClient(workOrderDTORequest.getClient().getIdentification())
                .orElseGet(() -> clientService.saveClient(
                         Client.builder()
                                 .idClient(workOrderDTORequest.getClient().getIdentification())
                                 .clientName(workOrderDTORequest.getClient().getName())
                                 .cliPhoneNumber(workOrderDTORequest.getClient().getCellphone())
                                 .build())
                );

        Administrator attendedBy = adminService.findAdministratorById(workOrderDTORequest.getAttendedById())
                .orElseThrow(() -> new NotFoundException(String.format("Administrator %s not found", workOrderDTORequest.getAttendedById())));

/*      WorkOrder workOrder = workOrderRepository.save(
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

        double totalValue = workOrderDTORequest.getServices().stream()
                .mapToDouble(ServicesDTO::getPrice)
                .sum();
        workOrder.setTotalValue(totalValue);
        workOrder.setBalance(totalValue - workOrderDTORequest.getDownPayment());

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
        Long idClient = workOrderDTORequest.getClient().getIdentification();

        if (nit.isEmpty()) {
            throw new NotFoundException(String.format("La compañía con nit %s no esta registrada!!", nit));
        }

        if (ObjectUtils.isEmpty(idClient)) {
            throw new NotFoundException("Se requiere identificación del cliente!!");
        }
    }
}
