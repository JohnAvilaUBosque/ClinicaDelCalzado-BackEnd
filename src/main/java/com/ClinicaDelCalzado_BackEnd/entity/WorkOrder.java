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
@Table(name = "work_order")
public class WorkOrder {

    @Id
    @Column(name = "order_number", length = 20)
    private String orderNumber;

    @ManyToOne
    @JoinColumn(name = "id_company")
    private Company idCompany;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @Column(name = "order_status", length = 10)
    private String orderStatus;

    @Column(name = "payment_status", length = 10)
    private String paymentStatus;

    @ManyToOne
    @JoinColumn(name = "attended_by")
    private Administrator attendedBy;

    @ManyToOne
    @JoinColumn(name = "id_client")
    private Client idClient;

    @Column(name = "deposit")
    private Double deposit;

    @Column(name = "total_value")
    private Double totalValue;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "general_comment")
    private String generalComment;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idOrderSer")
    private List<Service> servicesList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idOrderCom")
    private List<Comment> commentsList;
}