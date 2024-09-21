package com.ClinicaDelCalzado_BackEnd.services.impl;

import com.ClinicaDelCalzado_BackEnd.dtos.enums.OrderStatusEnum;
import com.ClinicaDelCalzado_BackEnd.dtos.enums.PaymentStatusEnum;
import com.ClinicaDelCalzado_BackEnd.dtos.enums.ServicesStatusEnum;
import com.ClinicaDelCalzado_BackEnd.dtos.request.WorkOrderDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.response.*;
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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

        Company company = companyService.findCompanyWorkOrder(workOrderDTORequest);
        Client client = clientService.findClientWorkOrder(workOrderDTORequest);

        Administrator attendedBy = adminService.findAdministratorById(workOrderDTORequest.getAttendedById())
                .orElseThrow(() -> new NotFoundException(String.format("Administrator %s not found", workOrderDTORequest.getAttendedById())));

        double totalPriceOrder = totalPrice(workOrderDTORequest.getServices());

        WorkOrder workOrder = saveWorkOrder(
                WorkOrder.builder()
                        .orderNumber(String.format("%s-%s-%d", ORDER_ABR, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), generateRandomValueOrder()))
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

        commentService.saveCommentOrder(workOrderDTORequest, workOrder);
        saveServicesWorkOrder(workOrderDTORequest, workOrder);

        return new WorkOrderDTOResponse("Orden de trabajo creada exitosamente!", workOrder.getOrderNumber());
    }

    @Override
    public OrderByIdNumberDTOResponse getWorkOrderByOrderNumber(String orderNumber) {

        WorkOrder workOrder = getOrder(orderNumber);
        Optional<Company> company = companyService.findCompanyByNit(workOrder.getIdCompany().getNit());
        Optional<Client> client = clientService.findClientByIdClient(workOrder.getIdClient().getIdClient());
        Optional<Administrator> attendedBy = adminService.findAdministratorById(workOrder.getAttendedBy().getIdAdministrator());
        List<ServicesDTO> servicesDTOList = productService.getServicesOrder(orderNumber);
        List<CommentDTO> commentDTOList = commentService.getCommentOrder(orderNumber);

        CompanyDTO companyDTO = company.map(value -> CompanyDTO
                .builder()
                .name(value.getName())
                .nit(value.getNit())
                .address(value.getAddress())
                .phones(Collections.singletonList(value.getPhones()))
                .build()).orElse(null);

        ClientDTO clientDTO = client.map(value -> ClientDTO
                .builder()
                .identification(value.getIdClient())
                .name(value.getClientName())
                .cellphone(value.getCliPhoneNumber())
                .build()).orElse(null);

        return OrderByIdNumberDTOResponse
                .builder()
                .orderNumber(orderNumber)
                .company(companyDTO)
                .attendedBy(attendedBy.map(Administrator::getAdminName).orElse(""))
                .createDate(workOrder.getCreationDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .orderStatus(OrderStatusEnum.getValue(workOrder.getOrderStatus()))
                .deliveryDate(workOrder.getDeliveryDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .client(clientDTO)
                .services(servicesDTOList)
                .comments(commentDTOList)
                .downPayment(workOrder.getDeposit().longValue())
                .totalValue(workOrder.getTotalValue().longValue())
                .balance(workOrder.getBalance().longValue())
                .paymentStatus(PaymentStatusEnum.getValue(workOrder.getPaymentStatus()))
                .build();
    }

    @Override
    public OrderListDTOResponse getWorkOrderList(String orderStatus, String orderNumber, Long identification, String name, String phone, String attendedBy) {

        List<Object[]> workOrderList;

        if (!ObjectUtils.isEmpty(orderNumber) || !ObjectUtils.isEmpty(identification) || !ObjectUtils.isEmpty(name)
                || !ObjectUtils.isEmpty(phone) || !ObjectUtils.isEmpty(attendedBy)) {
            workOrderList = workOrderRepository.findFilteredWorkOrders(orderStatus, orderNumber, identification, name, phone, attendedBy);
        } else {
            workOrderList = workOrderRepository.findOrdersWithServicesByStatus(orderStatus);
        }

        return getOrderList(workOrderList);
    }

    private WorkOrder getOrder(String orderNumber) {
        Optional<WorkOrder> workOrder = workOrderRepository.findById(orderNumber);

        if (workOrder.isEmpty()) {
            throw new NotFoundException(String.format("La orden %s no esta registrada!!", orderNumber));
        }

        return workOrder.get();
    }

    private OrderListDTOResponse getOrderList(List<Object[]> workOrderList) {

        // Mapear los resultados de Object[] a una estructura de datos más manejable
        Map<String, List<Object[]>> ordersGroupedByOrderNumber = workOrderList.stream()
                .collect(Collectors.groupingBy(row -> (String) row[0]));  // Agrupa por orderNumber (posición 0)

        // Crear la respuesta
        OrderListDTOResponse orderListDTOResponse = new OrderListDTOResponse();

        // Convertir a OrderDTOResponse con conteo de servicios
        orderListDTOResponse.setOrders(
                ordersGroupedByOrderNumber.entrySet().stream()
                        .map(entry -> {
                            String orderNumber = entry.getKey();
                            List<Object[]> groupedOrders = entry.getValue();

                            // Obtener la primera orden para extraer los datos comunes
                            Object[] firstOrderRow = groupedOrders.get(0);

                            // Extraer datos comunes desde la primera fila del grupo
                            Long idClient = (Long) firstOrderRow[1];  // id_client (posición 1)
                            String clientName = (String) firstOrderRow[2]; // client_name (posición 2)
                            String clientPhone = (String) firstOrderRow[3]; // cli_phone_number (posición 3)
                            Timestamp creationDate = (Timestamp) firstOrderRow[4]; // creation_date (posición 4)
                            Date deliveryDate = (Date) firstOrderRow[5]; // delivery_date (posición 5)
                            long servicesOrderCount = (long) firstOrderRow[6]; // services_count (posición 6)

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

                            // Construir el OrderDTOResponse
                            return OrderDTOResponse.builder()
                                    .orderNumber(orderNumber)
                                    .client(ClientDTO.builder()
                                            .identification(idClient)
                                            .name(clientName)
                                            .cellphone(clientPhone)
                                            .build())
                                    .createDate(creationDate.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                                    .deliveryDate(simpleDateFormat.format(deliveryDate))
                                    .servicesCount(servicesOrderCount) // Cantidad total de servicios
                                    .orderStatus(OrderStatusEnum.getValue(String.valueOf(firstOrderRow[7]))) // order_status (posición 7)
                                    .totalValue(((Double) firstOrderRow[8]).longValue()) // total_value (posición 8)
                                    .downPayment(((Double) firstOrderRow[9]).longValue()) // deposit (posición 9)
                                    .balance(((Double) firstOrderRow[10]).longValue())  // balance (posición 10)
                                    .paymentStatus(PaymentStatusEnum.getValue(String.valueOf(firstOrderRow[11])))  // payment_status (posición 11)
                                    .build();
                        })
                        .toList()
        );

        return orderListDTOResponse;
    }

    private List<ServicesDTO> saveServicesWorkOrder(WorkOrderDTORequest workOrderDTORequest, WorkOrder orderNumber) {

        return workOrderDTORequest.getServices().stream()
                .map(serviceDTO -> {
                    ServicesEnt service = ServicesEnt.builder()
                            .idOrderSer(orderNumber)
                            .service(serviceDTO.getName())
                            .unitValue(serviceDTO.getPrice().doubleValue())
                            .serviceStatus(ServicesStatusEnum.RECEIVED.getKeyName())
                            .build();
                    productService.save(service);
                    return new ServicesDTO(service.getService(), service.getUnitValue().longValue(), service.getServiceStatus());
                }).collect(Collectors.toList());
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

        if (generalComment.isEmpty()) {
            throw new BadRequestException("Se requiere el comentario general de la orden de trabajo!!");
        }

        if (ObjectUtils.isEmpty(downPayment)) {
            throw new BadRequestException("Se requiere el abono del cliente!!");
        }
    }
}
