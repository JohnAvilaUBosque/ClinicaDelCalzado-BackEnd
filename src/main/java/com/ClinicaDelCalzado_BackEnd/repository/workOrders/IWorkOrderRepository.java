package com.ClinicaDelCalzado_BackEnd.repository.workOrders;

import com.ClinicaDelCalzado_BackEnd.entity.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IWorkOrderRepository extends JpaRepository<WorkOrder, String> {

    @Query(value = "SELECT wo.order_number, cl.id_client, cl.client_name, cl.cli_phone_number, " +
            "wo.creation_date, wo.delivery_date, " +
            "COUNT(se.id_order) AS services, " +
            "wo.order_status, wo.total_value, wo.deposit, wo.balance, wo.payment_status, " +
            "ad.id_administrator AS attended_by, co.id_company " +
            "FROM work_order wo " +
            "INNER JOIN client cl ON wo.id_client = cl.id_client " +
            "INNER JOIN service se ON wo.order_number = se.id_order " +
            "INNER JOIN administrator ad ON wo.attended_by = ad.id_administrator " +
            "INNER JOIN company co ON wo.id_company = co.id_company " +
            "WHERE wo.order_status = :orderStatus " +
            "AND wo.creation_date >= DATE_SUB(NOW(), INTERVAL 3 MONTH) " +
            "GROUP BY wo.order_number, cl.id_client, cl.client_name, cl.cli_phone_number, " +
            "wo.creation_date, wo.delivery_date, wo.order_status, wo.total_value," +
            " wo.deposit, wo.balance, wo.payment_status, ad.id_administrator, co.id_company",
            nativeQuery = true)
    List<Object[]> findOrdersWithServicesByStatus(@Param("orderStatus") String orderStatus);

    @Query(value = " SELECT wo.order_number, cl.id_client, cl.client_name, cl.cli_phone_number, " +
            "wo.creation_date, wo.delivery_date, " +
            "COUNT(se.id_order) AS services, " +
            "wo.order_status, wo.total_value, wo.deposit, wo.balance, wo.payment_status, " +
            "ad.id_administrator AS attended_by, co.id_company " +
            "FROM work_order wo " +
            "INNER JOIN client cl ON wo.id_client = cl.id_client " +
            "INNER JOIN service se ON wo.order_number = se.id_order " +
            "INNER JOIN administrator ad ON wo.attended_by = ad.id_administrator " +
            "INNER JOIN company co ON wo.id_company = co.id_company " +
            "WHERE wo.order_status = :orderStatus " +
            "AND ((:orderNumber IS NULL OR wo.order_number = :orderNumber)" +
            "AND (:clientId IS NULL OR cl.id_client = :clientId) " +
            "AND (:clientName IS NULL OR cl.client_name = :clientName) " +
            "AND (:cliPhoneNumber IS NULL OR cl.cli_phone_number = :cliPhoneNumber) " +
            "AND (:adminName IS NULL OR ad.admin_name = :adminName)) " +
            "GROUP BY wo.order_number, cl.id_client, cl.client_name, cl.cli_phone_number, " +
            "wo.creation_date, wo.delivery_date, wo.order_status, wo.total_value, " +
            "wo.deposit, wo.balance, wo.payment_status, ad.id_administrator, co.id_company", nativeQuery = true)
    List<Object[]> findFilteredWorkOrders(@Param("orderStatus") String orderStatus, @Param("orderNumber") String orderNumber, @Param("clientId") Long clientId,
                                          @Param("clientName") String clientName, @Param("cliPhoneNumber") String cliPhoneNumber, @Param("adminName") String adminName);

}
