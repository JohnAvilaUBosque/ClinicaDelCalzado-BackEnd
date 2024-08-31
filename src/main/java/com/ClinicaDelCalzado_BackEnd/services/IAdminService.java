package com.ClinicaDelCalzado_BackEnd.services;

import com.ClinicaDelCalzado_BackEnd.dtos.Request.AdminDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.Response.AdminDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.Response.AdminListDTOResponse;
import com.ClinicaDelCalzado_BackEnd.entity.Administrator;

import java.util.Optional;

public interface IAdminService {

    Optional<Administrator> findAdministratorById(Long idAdministrator);

    AdminDTOResponse findAdministratorByIdAdmin(Long idAdministrator);

    AdminListDTOResponse findAdministratorAll();

    AdminDTOResponse create(AdminDTORequest adminDTO);

    AdminDTOResponse update(Long adminId, AdminDTORequest adminDTO);

}
