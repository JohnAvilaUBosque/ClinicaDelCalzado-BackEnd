package com.ClinicaDelCalzado_BackEnd.repository.workOrders;

import com.ClinicaDelCalzado_BackEnd.entity.Company;
import com.ClinicaDelCalzado_BackEnd.exceptions.RepositoryException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICompanyRepository extends JpaRepository<Company, Integer> {

    Company findByNit(String nit);

    @Query(value = "SELECT * FROM company c WHERE c.id_company = :id_company AND UPPER(c.nit) = UPPER(:nit)", nativeQuery = true)
    Company findCompanyByIdCompanyAndNit(@Param("id_company") Integer idCompany, @Param("nit") String nit) throws RepositoryException;

}
