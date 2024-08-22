package com.ClinicaDelCalzado_BackEnd.services.impl;

import com.ClinicaDelCalzado_BackEnd.dtos.Request.WorkOrderDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.Response.WorkOrderDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.workOrders.OrderErrorDTO;
import com.ClinicaDelCalzado_BackEnd.entity.Company;
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

    private Optional<Company> company;
    private final List<OrderErrorDTO> orderErrors = new ArrayList<>();

//    @Autowired
//    public WorkOrderServiceImpl(IWorkOrderRepository workOrderRepository, ICompanyRepository companyRepository,
//                                IClientRepository clientRepository, IServiceRepository serviceRepository,
//                                IAdministratorRepository administratorRepository) {
//        this.workOrderRepository = workOrderRepository;
//        this.companyRepository = companyRepository;
//        this.clientRepository = clientRepository;
//        this.serviceRepository = serviceRepository;
//        this.administratorRepository = administratorRepository;
//        this.mapper = new ModelMapper();
//    }

    @Override
    public WorkOrderDTOResponse createWorkOrder(WorkOrderDTORequest workOrderDTO) {
        return null;
    }
//
//        orderErrors.clear();
//        try {
//
//        } catch (RepositoryException ex) {
//            throw ex;
//        }
//        // Buscar o crear la empresa
//        //Company company = companyRepository.findByNit(requestDTO.getCompany().getNit());
////        if (company == null) {
//            company = new Company();
//            company.setName(requestDTO.getCompany().getName());
//            company.setNit(requestDTO.getCompany().getNit());
//            company.setAddress(requestDTO.getCompany().getAddress());
//            company.setPhones(String.join(",", requestDTO.getCompany().getPhones()));
//            companyRepository.save(company);
//        }

        // Buscar o crear el cliente
//        Client client = clientRepository.findByIdentification(requestDTO.getClient().getIdentification());
//        if (client == null) {
//            client = new Client();
//            client.setClientName(requestDTO.getClient().getName());
//            client.setCliPhoneNumber(requestDTO.getClient().getCellphone());
//            clientRepository.save(client);
//        }
//
//        // Buscar el administrador
//        Administrator attendedBy = administratorRepository.findById(requestDTO.getAttendedById()).orElseThrow(() -> new RuntimeException("Administrator not found"));
//
//        // Crear la orden de trabajo
//        WorkOrder workOrder = new WorkOrder();
//        workOrder.setOrderNumber(requestDTO.getOrderNumber());
//        workOrder.setCreationDate(LocalDate.parse(requestDTO.getCreateDate()));
//        workOrder.setDeliveryDate(LocalDate.parse(requestDTO.getDeliveryDate()));
//        workOrder.setOrderStatus(requestDTO.getOrderStatus());
//        workOrder.setPaymentStatus(requestDTO.getServiceStatus());
//        workOrder.setAttendedBy(attendedBy);
//        workOrder.setIdClient(client);
//        workOrder.setIdCompany(company);
//        workOrder.setDeposit(requestDTO.getClient().getDownPayment());
//
//        // Calcular el total y saldo
//        Double totalValue = requestDTO.getClient().getServices().stream().mapToDouble(ServiceDTO::getPrice).sum();
//        workOrder.setTotalValue(totalValue);
//        workOrder.setBalance(totalValue - requestDTO.getClient().getDownPayment());
//
//        workOrderRepository.save(workOrder);
//
//        // Guardar los servicios
//        for (ServiceDTO serviceDTO : requestDTO.getClient().getServices()) {
//            Service service = new Service();
//            service.setService(serviceDTO.getName());
//            service.setUnitValue(serviceDTO.getPrice());
//            service.setServiceStatus(requestDTO.getServiceStatus());
//            service.setIdOrderSer(workOrder);
//            serviceRepository.save(service);
//        }
//
//        return workOrder;
//    }
//
//
//    private void validateCompany(String companyNit) {
//
//        Company company = companyRepository.findByNit(companyNit);
//        if (company == null) {
//            company = new Company();
//            company.setName(requestDTO.getCompany().getName());
//            company.setNit(requestDTO.getCompany().getNit());
//            company.setAddress(requestDTO.getCompany().getAddress());
//            company.setPhones(String.join(",", requestDTO.getCompany().getPhones()));
//            companyRepository.save(company);
//        }
//        user = userService.getUser(buyerId, RolE.BUYER.name());
//        //Validar si el usuario buyer esta registrado para crear la orden de compra
//        if (user
//                .isEmpty()) {
//            throw new NotFoundException(String.format("La compañía %s no esta registrada!!", companyId));
//        }
//    }
}
