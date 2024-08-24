package com.ClinicaDelCalzado_BackEnd.services.impl;

import com.ClinicaDelCalzado_BackEnd.dtos.Request.WorkOrderDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.Response.WorkOrderDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.workOrders.*;
import com.ClinicaDelCalzado_BackEnd.entity.*;
import com.ClinicaDelCalzado_BackEnd.exceptions.RepositoryException;
import com.ClinicaDelCalzado_BackEnd.repository.userAdmin.IAdministratorRepository;
import com.ClinicaDelCalzado_BackEnd.repository.workOrders.IClientRepository;
import com.ClinicaDelCalzado_BackEnd.repository.workOrders.ICompanyRepository;
import com.ClinicaDelCalzado_BackEnd.repository.workOrders.IServiceRepository;
import com.ClinicaDelCalzado_BackEnd.repository.workOrders.IWorkOrderRepository;
import com.ClinicaDelCalzado_BackEnd.services.IWorkOrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WorkOrderServiceImpl implements IWorkOrderService {

    private IWorkOrderRepository workOrderRepository;

    private ICompanyRepository companyRepository;

    private IClientRepository clientRepository;

    private IServiceRepository serviceRepository;

    private IAdministratorRepository administratorRepository;

    ModelMapper mapper;

    //private Optional<Company> company;
    private final List<OrderErrorDTO> orderErrors = new ArrayList<>();

    @Autowired
    public WorkOrderServiceImpl(IWorkOrderRepository workOrderRepository, ICompanyRepository companyRepository,
                                IClientRepository clientRepository, IServiceRepository serviceRepository,
                                IAdministratorRepository administratorRepository) {
        this.workOrderRepository = workOrderRepository;
        this.companyRepository = companyRepository;
        this.clientRepository = clientRepository;
        this.serviceRepository = serviceRepository;
        this.administratorRepository = administratorRepository;
        this.mapper = new ModelMapper();
    }

    @Override
    public WorkOrderDTOResponse createWorkOrder(WorkOrderDTORequest workOrderDTO) {

        // Buscar o crear la empresa
        Company company = companyRepository.findByNit(workOrderDTO.getWorkOrder().getCompany().getNit());
        if (company == null) {
            company = new Company();
            company.setName(workOrderDTO.getWorkOrder().getCompany().getName());
            company.setNit(workOrderDTO.getWorkOrder().getCompany().getNit());
            company.setAddress(workOrderDTO.getWorkOrder().getCompany().getAddress());
            company.setPhones(String.join(",", workOrderDTO.getWorkOrder().getCompany().getPhones()));
            companyRepository.save(company);
        }

        // Buscar o crear el cliente
        Client client = clientRepository.findByIdentification(workOrderDTO.getWorkOrder().getClient().getIdentification());
        if (client == null) {
            client = new Client();
            client.setClientName(workOrderDTO.getWorkOrder().getClient().getName());
            client.setCliPhoneNumber(workOrderDTO.getWorkOrder().getClient().getCellphone());
            clientRepository.save(client);
        }

        // Buscar el administrador
        Administrator attendedBy = administratorRepository.findById(workOrderDTO.getWorkOrder().getAttendedById()).orElseThrow(() -> new RuntimeException("Administrator not found"));

        // Crear la orden de trabajo
        WorkOrder workOrder = new WorkOrder();
        workOrder.setOrderNumber(workOrderDTO.getWorkOrder().getOrderNumber());
        workOrder.setCreationDate(LocalDate.parse(workOrderDTO.getWorkOrder().getCreateDate().toString()));
        workOrder.setDeliveryDate(LocalDate.parse(workOrderDTO.getWorkOrder().getDeliveryDate().toString()));
        workOrder.setOrderStatus(workOrderDTO.getWorkOrder().getOrderStatus());
        workOrder.setPaymentStatus(workOrderDTO.getWorkOrder().getPaymentStatus());
        workOrder.setAttendedBy(attendedBy);
        workOrder.setIdClient(client);
        workOrder.setIdCompany(company);
        workOrder.setDeposit(workOrderDTO.getWorkOrder().getDownPayment());

        // Calcular el total y saldo
        Double totalValue = workOrderDTO.getWorkOrder().getServices().stream().mapToDouble(ServiceDTO::getPrice).sum();
        workOrder.setTotalValue(totalValue);
        workOrder.setBalance(totalValue - workOrderDTO.getWorkOrder().getDownPayment());

        workOrderRepository.save(workOrder);

        // Guardar los servicios
        List<ServiceDTO> services = new ArrayList<>();
        for (ServiceDTO serviceDTO : workOrderDTO.getWorkOrder().getServices()) {
            ServicesEnt service = new ServicesEnt();
            service.setService(serviceDTO.getName());
            service.setUnitValue(serviceDTO.getPrice());
            service.setServiceStatus(workOrderDTO.getWorkOrder().getOrderStatus());
            service.setIdOrderSer(workOrder);
            serviceRepository.save(service);

            services.add(new ServiceDTO(serviceDTO.getName(), serviceDTO.getPrice(), serviceDTO.getServiceStatus()));
        }

        // Crear el DTO de respuesta
        ClientDTO clientResponseDTO = new ClientDTO(client.getIdClient(), client.getClientName(), client.getCliPhoneNumber());
        WorkOrderDTORes orderResponseDTO = new WorkOrderDTORes(
                workOrder.getOrderNumber(),
                attendedBy.getAdminName(),
                workOrder.getCreationDate(),
                workOrder.getOrderStatus(),
                workOrder.getDeliveryDate(),
                clientResponseDTO,
                services,
                workOrder.getCommentsList().toString(),
                workOrder.getDeposit(),
                workOrder.getTotalValue(),
                workOrder.getBalance(),
                workOrder.getPaymentStatus()
        );

        return new WorkOrderDTOResponse("Orden de trabajo creada exitosamente!", orderResponseDTO);
    }

//    private boolean validateCompany(String nit) {
//
//        company = companyRepository.findByNit(nit);
//
//        //Validar si la compañia esta registrada
//        if (company.isEmpty()) {
//            throw new NotFoundException(String.format("La compañía %s no esta registrada!!", nit));
//        }
//        return false;
//    }
}
