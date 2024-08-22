package com.ClinicaDelCalzado_BackEnd.services.impl;

import com.ClinicaDelCalzado_BackEnd.entity.Company;
import com.ClinicaDelCalzado_BackEnd.repository.workOrders.ICompanyRepository;
import com.ClinicaDelCalzado_BackEnd.services.ICompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyServiceImpl implements ICompanyService {

    private final ICompanyRepository companyRepository;

    @Autowired
    public CompanyServiceImpl (ICompanyRepository companyRepository){
        this.companyRepository = companyRepository;
    }

    @Override
    public Optional<Company> getCompany(Integer companyId, String nit){
        return Optional.ofNullable(companyRepository.findUserByCompanyIdAndNit(companyId, nit));
    }

    @Override
    public Company findByNit(String nit){
        return null;
    }


}
