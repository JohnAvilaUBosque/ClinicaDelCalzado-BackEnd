package com.ClinicaDelCalzado_BackEnd.repository.workOrders;

import com.ClinicaDelCalzado_BackEnd.entity.Company;
import com.ClinicaDelCalzado_BackEnd.exceptions.RepositoryException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ICompanyRepository extends JpaRepository<Company, Integer> {

    Company findByNit(String nit);

    @Query(value = "SELECT * FROM ", nativeQuery = true)
    Company findUserByCompanyIdAndNit(@Param("user_id") Integer userId, @Param("rol_name") String rolName) throws RepositoryException;
}
