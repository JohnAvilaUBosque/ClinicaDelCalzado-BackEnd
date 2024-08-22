package com.ClinicaDelCalzado_BackEnd.services;

import com.ClinicaDelCalzado_BackEnd.entity.Company;

import java.util.Optional;

public interface ICompanyService {

    Optional<Company> getCompany(Integer companyId, String nit);
    Company findByNit(String nit);
}
