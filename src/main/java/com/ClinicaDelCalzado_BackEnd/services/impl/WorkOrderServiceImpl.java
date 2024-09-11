package com.ClinicaDelCalzado_BackEnd.services.impl;

import com.ClinicaDelCalzado_BackEnd.dtos.Request.WorkOrderDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.Response.WorkOrderDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.workOrders.*;
import com.ClinicaDelCalzado_BackEnd.entity.*;
import com.ClinicaDelCalzado_BackEnd.exceptions.BadRequestException;
import com.ClinicaDelCalzado_BackEnd.repository.workOrders.IServicesRepository;
import com.ClinicaDelCalzado_BackEnd.repository.workOrders.IWorkOrderRepository;
import com.ClinicaDelCalzado_BackEnd.services.IAdminService;
import com.ClinicaDelCalzado_BackEnd.services.IClientService;
import com.ClinicaDelCalzado_BackEnd.services.ICompanyService;
import com.ClinicaDelCalzado_BackEnd.services.IWorkOrderService;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.webjars.NotFoundException;

import java.time.LocalDate;
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

        Company company = findCompanyWorkOrder(workOrderDTORequest);
        Client client = findClientWorkOrder(workOrderDTORequest);

        Administrator attendedBy = adminService.findAdministratorById(workOrderDTORequest.getAttendedById())
                .orElseThrow(() -> new NotFoundException(String.format("Administrator %s not found", workOrderDTORequest.getAttendedById())));

        double totalPriceOrder = totalPrice(workOrderDTORequest.getServices());
/*
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
                        totalPriceOrder,
                        totalPriceOrder - workOrderDTORequest.getDownPayment(),
                        null   // General comment (if needed)
                )
        );

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

    private Company findCompanyWorkOrder(WorkOrderDTORequest workOrderDTORequest) {

        return companyService.findCompanyByNit(workOrderDTORequest.getCompany().getNit())
                .orElseGet(() -> companyService.save(
                        Company.builder()
                                .nit(workOrderDTORequest.getCompany().getNit())
                                .name(workOrderDTORequest.getCompany().getName())
                                .address(workOrderDTORequest.getCompany().getAddress())
                                .phones(String.join(",", workOrderDTORequest.getCompany().getPhones()))
                                .build())
                );
    }

    private Client findClientWorkOrder(WorkOrderDTORequest workOrderDTORequest) {

        return clientService.findClientByIdClient(workOrderDTORequest.getClient().getIdentification())
                .orElseGet(() -> clientService.saveClient(
                        Client.builder()
                                .idClient(workOrderDTORequest.getClient().getIdentification())
                                .clientName(workOrderDTORequest.getClient().getName())
                                .cliPhoneNumber(workOrderDTORequest.getClient().getCellphone())
                                .build())
                );
    }

    /*private void save() {
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
    }*/

    private Double totalPrice(List<ServicesDTO> servicesDTO) {
        return servicesDTO.stream().mapToDouble(ServicesDTO::getPrice).sum();
    }

    private void validateRequest(WorkOrderDTORequest workOrderDTORequest) {
        String nit = workOrderDTORequest.getCompany().getNit();
        Long attended = workOrderDTORequest.getAttendedById();
        LocalDate deliveryDate = workOrderDTORequest.getDeliveryDate();
        Long idClient = workOrderDTORequest.getClient().getIdentification();
        List<ServicesDTO> services = workOrderDTORequest.getServices();
        String generalComment = workOrderDTORequest.getGeneralComment();
        Double downPayment = workOrderDTORequest.getDownPayment();

        if (nit.isEmpty()) {
            throw new BadRequestException(String.format("La compañía con nit %s no esta registrada!!", nit));
        }

        if (ObjectUtils.isEmpty(attended)) {
            throw new BadRequestException("Se requiere identificación del administrador que esta atendiendo!!");
        }

        if (ObjectUtils.isEmpty(deliveryDate)) {
            throw new BadRequestException("Se requiere la fecha de entrega para la orden de trabajo!!");
        }

        if (ObjectUtils.isEmpty(idClient)) {
            throw new BadRequestException("Se requiere identificación del cliente!!");
        }

        if (CollectionUtils.isEmpty(services)) {
            throw new BadRequestException("Se requiere el listado de servicios para la orden de trabajo!!");
        }

        if (generalComment.isEmpty()){
            throw new BadRequestException("Se requiere el comentario general de la orden de trabajo!!");
        }

        if (ObjectUtils.isEmpty(downPayment)) {
            throw new BadRequestException("Se requiere el abono del cliente!!");
        }
    }
}
