package com.ClinicaDelCalzado_BackEnd.services.impl;

import com.ClinicaDelCalzado_BackEnd.dtos.enums.OrderStatusEnum;
import com.ClinicaDelCalzado_BackEnd.dtos.enums.PaymentStatusEnum;
import com.ClinicaDelCalzado_BackEnd.dtos.enums.ServicesStatusEnum;
import com.ClinicaDelCalzado_BackEnd.dtos.request.WorkOrderDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.response.WorkOrderDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.workOrders.*;
import com.ClinicaDelCalzado_BackEnd.entity.*;
import com.ClinicaDelCalzado_BackEnd.exceptions.BadRequestException;
import com.ClinicaDelCalzado_BackEnd.repository.workOrders.IWorkOrderRepository;
import com.ClinicaDelCalzado_BackEnd.services.*;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.ClinicaDelCalzado_BackEnd.util.Constants.*;


@Service
public class WorkOrderServiceImpl implements IWorkOrderService {

    private final IWorkOrderRepository workOrderRepository;

    private final ICompanyService companyService;

    private final IClientService clientService;

    private final IProductService productService;

    private final IAdminService adminService;

    private final ICommentService commentService;

    //private Optional<Company> company;
    private final List<OrderErrorDTO> orderErrors = new ArrayList<>();

    @Autowired
    public WorkOrderServiceImpl(IWorkOrderRepository workOrderRepository, ICompanyService companyService,
                                IClientService clientService, IProductService productService,
                                IAdminService adminService, ICommentService commentService) {
        this.workOrderRepository = workOrderRepository;
        this.companyService = companyService;
        this.clientService = clientService;
        this.productService = productService;
        this.adminService = adminService;
        this.commentService = commentService;
    }

    @Override
    public WorkOrderDTOResponse createWorkOrder(WorkOrderDTORequest workOrderDTORequest) {

        validateRequest(workOrderDTORequest);

        Company company = findCompanyWorkOrder(workOrderDTORequest);
        Client client = findClientWorkOrder(workOrderDTORequest);

        Administrator attendedBy = adminService.findAdministratorById(workOrderDTORequest.getAttendedById())
                .orElseThrow(() -> new NotFoundException(String.format("Administrator %s not found", workOrderDTORequest.getAttendedById())));

        double totalPriceOrder = totalPrice(workOrderDTORequest.getServices());

        WorkOrder workOrder = saveWorkOrder(
                WorkOrder.builder()
                        .orderNumber(String.format("%s-%s-%d",ORDER_ABR,LocalDate.now().getYear(), generateRandomValueOrder()))
                        .idCompany(company)
                        .creationDate(LocalDateTime.now())
                        .deliveryDate(LocalDate.parse(workOrderDTORequest.getDeliveryDate().toString()))
                        .orderStatus(OrderStatusEnum.VALID.getKeyName())
                        .paymentStatus(totalPriceOrder == 0 ? PaymentStatusEnum.PAID.getKeyName() : PaymentStatusEnum.PENDING.getKeyName())
                        .attendedBy(attendedBy)
                        .idClient(client)
                        .deposit(workOrderDTORequest.getDownPayment())
                        .totalValue(totalPriceOrder)
                        .balance(totalPriceOrder - workOrderDTORequest.getDownPayment())
                        .build());

        saveCommentOrder(workOrderDTORequest, workOrder);
        saveServicesWorkOrder(workOrderDTORequest, workOrder);

        return new WorkOrderDTOResponse("Orden de trabajo creada exitosamente!", workOrder.getOrderNumber());
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

    private List<ServicesDTO> saveServicesWorkOrder(WorkOrderDTORequest workOrderDTORequest, WorkOrder orderNumber) {

        return  workOrderDTORequest.getServices().stream()
                .map(serviceDTO -> {
                    ServicesEnt service = ServicesEnt.builder()
                            .idOrderSer(orderNumber)
                            .service(serviceDTO.getName())
                            .unitValue(serviceDTO.getPrice())
                            .serviceStatus(ServicesStatusEnum.RECEIVED.getKeyName())
                            .build();
                    productService.save(service);
                    return new ServicesDTO(service.getService(), service.getUnitValue(), service.getServiceStatus());
                }).collect(Collectors.toList());
    }

    private Comment saveCommentOrder(WorkOrderDTORequest workOrderDTORequest, WorkOrder orderNumber) {
        return commentService.save(Comment.builder()
                .idOrderCom(WorkOrder.builder().orderNumber(orderNumber.getOrderNumber()).build())
                .adminComment(workOrderDTORequest.getGeneralComment())
                .build());
    }

    private Client findClientWorkOrder(WorkOrderDTORequest workOrderDTORequest) {

        Optional<Client> clientByIdClient = clientService.findClientByIdClient(workOrderDTORequest.getClient().getIdentification());
        Client client = Client.builder()
                .idClient(workOrderDTORequest.getClient().getIdentification())
                .clientName(workOrderDTORequest.getClient().getName())
                .cliPhoneNumber(workOrderDTORequest.getClient().getCellphone())
                .build();

        if (clientByIdClient.isEmpty() || !clientService.validateDifferenceData(clientByIdClient.get(), client)) {
            return clientService.saveClient(client);
        }

        return clientByIdClient.get();
    }

    private WorkOrder saveWorkOrder(WorkOrder workOrder) {
        return workOrderRepository.save(workOrder);
    }

    private Double totalPrice(List<ServicesDTO> servicesDTO) {
        return servicesDTO.stream().mapToDouble(ServicesDTO::getPrice).sum();
    }

    private Integer generateRandomValueOrder() {
        Random random = new Random();
        return RANDOM_MIN + random.nextInt(RANDOM_MAX - RANDOM_MIN + 1);
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
