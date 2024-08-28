package com.ClinicaDelCalzado_BackEnd.repository.workOrders;

import com.ClinicaDelCalzado_BackEnd.entity.ServicesEnt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IServicesRepository extends JpaRepository <ServicesEnt, Integer> {
}
