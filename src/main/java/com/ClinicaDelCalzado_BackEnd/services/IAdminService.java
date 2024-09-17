package com.ClinicaDelCalzado_BackEnd.services;

import com.ClinicaDelCalzado_BackEnd.dtos.request.AdminDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.request.UpdateAdminDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.request.UpdateAdminPasswordDTO;
import com.ClinicaDelCalzado_BackEnd.dtos.response.AdminDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.response.AdminListDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.response.UpdateAdminPasswordDTOResponse;
import com.ClinicaDelCalzado_BackEnd.entity.Administrator;

import java.util.Optional;

public interface IAdminService {

    Optional<Administrator> findAdministratorById(Long idAdministrator);

    AdminDTOResponse findAdministratorByIdAdmin(Long idAdministrator);

    AdminListDTOResponse findAdministratorAll();

    AdminDTOResponse create(AdminDTORequest adminDTO);

    AdminDTOResponse update(Long adminId, UpdateAdminDTORequest adminDTO);

    UpdateAdminPasswordDTOResponse updatePassword(Long adminId, UpdateAdminPasswordDTO adminDTO);

}
