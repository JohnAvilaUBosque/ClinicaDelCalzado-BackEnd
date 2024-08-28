package com.ClinicaDelCalzado_BackEnd.services;

import com.ClinicaDelCalzado_BackEnd.entity.Company;

import java.util.Optional;

public interface ICompanyService {

    Optional<Company> getCompany(Integer idCompany,String nit);
    Optional<Company> findCompanyByNit(String nit);
    Company save(Company company);
}
