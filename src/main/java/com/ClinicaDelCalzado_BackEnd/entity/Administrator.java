package com.ClinicaDelCalzado_BackEnd.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Administrator")
public class Administrator {

    @Id
    @Column(name = "id_administrator", length = 20)
    private Integer idAdministrator;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "role", length = 50)
    private String role;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "administrator")
    private List<Answer> answersList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "attendedBy")
    private List<WorkOrder> workOrdersList;
}
