package com.ClinicaDelCalzado_BackEnd.services.impl;

import com.ClinicaDelCalzado_BackEnd.dtos.Request.AdminDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.Response.AdminDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.enums.AdminStatusEnum;
import com.ClinicaDelCalzado_BackEnd.dtos.enums.AdminTypeEnum;
import com.ClinicaDelCalzado_BackEnd.dtos.userAdmin.AdminDTO;
import com.ClinicaDelCalzado_BackEnd.entity.Administrator;
import com.ClinicaDelCalzado_BackEnd.exceptions.AlreadyExistsException;
import com.ClinicaDelCalzado_BackEnd.repository.userAdmin.IAdministratorRepository;
import com.ClinicaDelCalzado_BackEnd.services.IAdminService;
import com.ClinicaDelCalzado_BackEnd.util.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminServiceImpl implements IAdminService {

    private final IAdministratorRepository administratorRepository;

    @Autowired
    public AdminServiceImpl(IAdministratorRepository administratorRepository) {
        this.administratorRepository = administratorRepository;
    }

    @Override
    public Optional<Administrator> findAdministratorById(Long idAdministrator) {
        return administratorRepository.findById(idAdministrator);
    }

    @Override
    public AdminDTOResponse create(AdminDTORequest adminDTO) {

        Long idAdministrator = 0L;

        idAdministrator = adminDTO.getIdentification();

        if (findAdministratorById(idAdministrator).isPresent()) {
            throw new AlreadyExistsException(
                    String.format("El administrador con identificador %s ya existe", idAdministrator));
        }

        Administrator administrator = Administrator.builder()
                .idAdministrator(idAdministrator)
                .adminName(adminDTO.getName())
                .AdminStatus(AdminStatusEnum.ACTIVE.name())
                .admPhoneNumber(adminDTO.getCellphone())
                .role(AdminTypeEnum.getValue(adminDTO.getAdminType()))
                .password(Encrypt.encode(adminDTO.getPassword()))
                .build();

        administratorRepository.save(administrator);

        return AdminDTOResponse.builder()
                .message("Administrador creado exitosamente.")
                .admin(AdminDTO.builder()
                        .identification(idAdministrator)
                        .adminType(adminDTO.getAdminType())
                        .name(adminDTO.getName())
                        .cellphone(adminDTO.getCellphone())
                        .build())
                .build();
    }
}
