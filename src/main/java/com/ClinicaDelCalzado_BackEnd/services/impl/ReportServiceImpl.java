package com.ClinicaDelCalzado_BackEnd.services.impl;

import com.ClinicaDelCalzado_BackEnd.dtos.enums.OrderStatusEnum;
import com.ClinicaDelCalzado_BackEnd.dtos.reports.DetailedReportDTO;
import com.ClinicaDelCalzado_BackEnd.dtos.reports.GeneralReportDTO;
import com.ClinicaDelCalzado_BackEnd.dtos.response.DetailedReportDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.response.GeneralReportDTOResponse;
import com.ClinicaDelCalzado_BackEnd.exceptions.BadRequestException;
import com.ClinicaDelCalzado_BackEnd.repository.workOrders.IWorkOrderRepository;
import com.ClinicaDelCalzado_BackEnd.services.IReportService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements IReportService {

    private final IWorkOrderRepository workOrderRepository;

    @Autowired
    public ReportServiceImpl(IWorkOrderRepository workOrderRepository) {
        this.workOrderRepository = workOrderRepository;
    }

    @Override
    public DetailedReportDTOResponse getWorkOrderList(List<String> orderStatus, LocalDateTime startDate, LocalDateTime endDate) {

        String orderStatusSeparated = String.join(",", orderStatus);
        String[] orderStatusSplit = orderStatusSeparated.split(",\\s*");

        if (!ObjectUtils.isEmpty(orderStatusSplit) || !ObjectUtils.isEmpty(startDate) || !ObjectUtils.isEmpty(endDate)) {
            List<Object[]> workOrderList = workOrderRepository.findWorkOrdersWithServices(orderStatusSplit, startDate, endDate);

            return getReportOrderList(workOrderList);
        } else {
            throw new BadRequestException("No se encontro información para el reporte detallado");
        }

    }

    @Override
    public GeneralReportDTOResponse getWorkOrderListGeneral(List<String> orderStatus, LocalDateTime startDate, LocalDateTime endDate) {

        String orderStatusSeparated = String.join(",", orderStatus);
        String[] orderStatusSplit = orderStatusSeparated.split(",\\s*");

        if (!ObjectUtils.isEmpty(orderStatusSplit) || !ObjectUtils.isEmpty(startDate) || !ObjectUtils.isEmpty(endDate)) {
            List<Object[]> workOrderList = workOrderRepository.findWorkOrdersWithServices(orderStatusSplit, startDate, endDate);

            return getReportOrderListGeneral(workOrderList);

        } else {
            throw new BadRequestException("No se encontro información para el reporte general!");
        }

    }

    private DetailedReportDTOResponse getReportOrderList(List<Object[]> workOrderList) {

        // Mapear los resultados de Object[] a una estructura de datos más manejable
        Map<String, List<Object[]>> ordersGroupedByOrderNumber = workOrderList.stream()
                .collect(Collectors.groupingBy(row -> (String) row[1]));

        // Crear la respuesta
        DetailedReportDTOResponse detailedReportDTOResponse = new DetailedReportDTOResponse();

        // Convertir a DetailedReportDTOResponse con conteo de servicios
        detailedReportDTOResponse.setOrders(
                ordersGroupedByOrderNumber.values().stream()
                        .map(groupedOrders -> {

                            // Obtener la primera orden para extraer los datos comunes
                            Object[] firstOrderRow = groupedOrders.get(0);

                            // Extraer datos comunes desde la primera fila del grupo
                            Timestamp creationDate = (Timestamp) firstOrderRow[0];  // creation_date (posición 0)
                            String orderNumber = (String) firstOrderRow[1];  // order_number (posición 1)

                            // Sumar totales por cada orden
                            double totalServicesValue = groupedOrders.stream().mapToDouble(row -> (Double) row[2]).sum(); // total_value (posición 2)
                            double totalDeposits = groupedOrders.stream().mapToDouble(row -> (Double) row[3]).sum(); // deposit (posición 3)
                            double totalBalance = groupedOrders.stream().mapToDouble(row -> (Double) row[4]).sum(); // balance (posición 4)

                            // Contar servicios en cada estado
                            long servicesReceived = groupedOrders.stream().mapToLong(row -> (long) row[5]).sum(); // services_received (posición 5)
                            long servicesCompleted = groupedOrders.stream().mapToLong(row -> (long) row[6]).sum(); // services_completed (posición 6)
                            long servicesDispatched = groupedOrders.stream().mapToLong(row -> (long) row[7]).sum(); // services_dispatched (posición 7)
                            String orderStatus = OrderStatusEnum.getValue((String) firstOrderRow[8]);

                            // Construir el OrderDTOResponse
                            return DetailedReportDTO.builder()
                                    .orderNumber(orderNumber)
                                    .creationDate(creationDate.toLocalDateTime())
                                    .totalServicesValue((long) totalServicesValue)
                                    .totalDeposits((long) totalDeposits)
                                    .totalBalance((long) totalBalance)
                                    .servicesReceived(servicesReceived)
                                    .servicesCompleted(servicesCompleted)
                                    .servicesDispatched(servicesDispatched)
                                    .orderStatus(orderStatus)
                                    .build();
                        })
                        .sorted(Comparator.comparing(DetailedReportDTO::getCreationDate))
                        .toList()
        );

        return detailedReportDTOResponse;
    }

    private GeneralReportDTOResponse getReportOrderListGeneral(List<Object[]> workOrderList) {

        // Mapear los resultados de Object[] a una estructura de datos más manejable
        Map<String, List<Object[]>> ordersGroupedByDate = workOrderList.stream()
                .collect(Collectors.groupingBy(row -> {
                    Timestamp creationDate = (Timestamp) row[0]; // Obtenemos la fecha de creación (posición 0)
                    return creationDate.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")); // Formato "dd-MM-yyyy"
                }));

        // Crear la respuesta
        GeneralReportDTOResponse generalReportDTOResponse = new GeneralReportDTOResponse();

        // Convertir a DetailedReportDTOResponse con conteo de servicios
        generalReportDTOResponse.setOrders(
                ordersGroupedByDate.values().stream()
                        .map(groupedOrders -> {

                            // Obtener la primera orden para extraer los datos comunes
                            Object[] firstOrderRow = groupedOrders.get(0);

                            // Extraer datos comunes desde la primera fila del grupo
                            Timestamp creationDate = (Timestamp) firstOrderRow[0];  // creation_date (posición 0)

                            // Sumar totales por cada orden
                            double totalServicesValue = groupedOrders.stream().mapToDouble(row -> (Double) row[2]).sum(); // total_value (posición 2)
                            double totalDeposits = groupedOrders.stream().mapToDouble(row -> (Double) row[3]).sum(); // deposit (posición 3)
                            double totalBalance = groupedOrders.stream().mapToDouble(row -> (Double) row[4]).sum(); // balance (posición 4)

                            // Contar servicios en cada estado
                            long servicesReceived = groupedOrders.stream().mapToLong(row -> (long) row[5]).sum(); // services_received (posición 5)
                            long servicesCompleted = groupedOrders.stream().mapToLong(row -> (long) row[6]).sum(); // services_completed (posición 6)
                            long servicesDispatched = groupedOrders.stream().mapToLong(row -> (long) row[7]).sum(); // services_dispatched (posición 7)

                            // Construir el OrderDTOResponse
                            return GeneralReportDTO.builder()
                                    .creationDate(creationDate)
                                    .totalServicesValue((long) totalServicesValue)
                                    .totalDeposits((long) totalDeposits)
                                    .totalBalance((long) totalBalance)
                                    .servicesReceived(servicesReceived)
                                    .servicesCompleted(servicesCompleted)
                                    .servicesDispatched(servicesDispatched)
                                    .build();
                        })
                        .sorted(Comparator.comparing(GeneralReportDTO::getCreationDate))
                        .toList()
        );

        return generalReportDTOResponse;

    }

}
