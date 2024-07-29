package com.ClinicaDelCalzado_BackEnd.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "WorkOrder")
public class WorkOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_number")
    private Integer orderNumber;

    @ManyToOne
    @JoinColumn(name = "id_company")
    private Company company;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @Column(name = "work_status", length = 50)
    private String workStatus;

    @Column(name = "order_status", length = 50)
    private String orderStatus;

    @Column(name = "payment_status", length = 50)
    private String paymentStatus;

    @ManyToOne
    @JoinColumn(name = "attended_by")
    private Administrator attendedBy;

    @ManyToOne
    @JoinColumn(name = "id_client")
    private Client client;

    @Column(name = "client_name", length = 100)
    private String clientName;

    @Column(name = "client_phone_number", length = 20)
    private String clientPhoneNumber;

    @Column(name = "product_list", columnDefinition = "TEXT")
    private String productList;

    @Column(name = "deposit")
    private Double deposit;

    @Column(name = "total_value")
    private Double totalValue;

    @Column(name = "balance")
    private Double balance;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "workOrder")
    private List<Service> servicesList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "workOrder")
    private List<Comment> commentsList;
}
