package com.ClinicaDelCalzado_BackEnd.services.impl;

import com.ClinicaDelCalzado_BackEnd.dtos.Request.AdminDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.Response.AdminDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.Response.AdminListDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.enums.AdminStatusEnum;
import com.ClinicaDelCalzado_BackEnd.dtos.enums.AdminTypeEnum;
import com.ClinicaDelCalzado_BackEnd.dtos.userAdmin.AdminDTO;
import com.ClinicaDelCalzado_BackEnd.entity.Administrator;
import com.ClinicaDelCalzado_BackEnd.exceptions.AlreadyExistsException;
import com.ClinicaDelCalzado_BackEnd.exceptions.BadRequestException;
import com.ClinicaDelCalzado_BackEnd.repository.userAdmin.IAdministratorRepository;
import com.ClinicaDelCalzado_BackEnd.services.IAdminService;
import com.ClinicaDelCalzado_BackEnd.util.Encrypt;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
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
    public AdminDTOResponse findAdministratorByIdAdmin(Long idAdministrator) {
        validateIdentification(idAdministrator);

        AdminDTOResponse adminDTOResponse = new AdminDTOResponse();
        Optional<Administrator> administrator = findAdministratorById(idAdministrator);

        if (administrator.isEmpty()) {
            throw new NotFoundException(String.format("La identificación %s del administrador no existe", idAdministrator));
        }

        adminDTOResponse.setMessage("Detalles del administrador recuperados exitosamente.");
        adminDTOResponse.setAdmin(administrator.map(p ->
                new AdminDTO(p.getIdAdministrator(), AdminTypeEnum.getValue(p.getRole()), p.getAdminName(),
                        p.getAdmPhoneNumber(), AdminStatusEnum.getValue(p.getAdminStatus()))).get());

        return adminDTOResponse;

    }

    @Override
    public AdminListDTOResponse findAdministratorAll() {

        List<Administrator> administratorList = administratorRepository.findAll();

        AdminListDTOResponse adminListDTOResponse = new AdminListDTOResponse();
        adminListDTOResponse.setAdmins(administratorList.stream().map(p ->
                new AdminDTO(p.getIdAdministrator(), AdminTypeEnum.getValue(p.getRole()), p.getAdminName(),
                        p.getAdmPhoneNumber(), AdminStatusEnum.getValue(p.getAdminStatus()))).toList());

        return adminListDTOResponse;
    }

    @Override
    public AdminDTOResponse create(AdminDTORequest adminDTO) {

        validateInputData(adminDTO, true);

        Long idAdministrator = adminDTO.getIdentification();

        if (findAdministratorById(idAdministrator).isPresent()) {
            throw new AlreadyExistsException(
                    String.format("El administrador con identificador %s ya existe", idAdministrator));
        }

        Administrator administrator = buildAdministrator(
                idAdministrator,
                adminDTO.getName(),
                adminDTO.getCellphone(),
                AdminStatusEnum.ACTIVE.getKeyName(),
                AdminTypeEnum.getName(adminDTO.getAdminType()),
                Encrypt.encode(adminDTO.getPassword()));

        saveAdmin(administrator);

        return adminDTOResponse("Administrador creado exitosamente.", administrator);
    }

    @Override
    public AdminDTOResponse update(Long adminId, AdminDTORequest adminDTO) {

        adminDTO.setIdentification(adminId);

        validateInputData(adminDTO, false);

        Optional<Administrator> administratorById = findAdministratorById(adminId);

        if (administratorById.isEmpty()) {
            throw new NotFoundException(String.format("El administrador con identificación %s no existe", adminId));
        }

        Administrator administrator = buildAdministrator(
                adminId,
                adminDTO.getName(),
                adminDTO.getCellphone(),
                administratorById.get().getAdminStatus(),
                administratorById.get().getRole(),
                administratorById.get().getPassword());

        saveAdmin(administrator);

        return adminDTOResponse("Administrador actualizado exitosamente.", administrator);
    }

    private Administrator buildAdministrator(Long adminId, String adminName, String adminPhoneNumber,
                                             String adminStatus, String role, String password) {
        return Administrator.builder()
                .idAdministrator(adminId)
                .adminName(adminName)
                .admPhoneNumber(adminPhoneNumber)
                .adminStatus(adminStatus)
                .role(role)
                .password(password)
                .build();
    }

    private void saveAdmin(Administrator administrator) {
        administratorRepository.save(administrator);
    }

    private AdminDTOResponse adminDTOResponse(String message, Administrator administrator) {
        return AdminDTOResponse.builder()
                .message(message)
                .admin(AdminDTO.builder()
                        .identification(administrator.getIdAdministrator())
                        .adminType(AdminTypeEnum.getValue(administrator.getRole()))
                        .status(AdminStatusEnum.getValue(administrator.getAdminStatus()))
                        .name(administrator.getAdminName())
                        .cellphone(administrator.getAdmPhoneNumber())
                        .build())
                .build();
    }

    private void validateInputData(AdminDTORequest adminDTO, Boolean validatePassword) {
        validateIdentification(adminDTO.getIdentification());

        if (ObjectUtils.isEmpty(adminDTO.getName())) {
            throw new BadRequestException("El nombre es un campo obligatorio, no puede ser vacío");
        }

        if (ObjectUtils.isEmpty(adminDTO.getAdminType())) {
            throw new BadRequestException("El tipo de administrador es un campo obligatorio, no puede ser vacío");
        }

        if (ObjectUtils.isEmpty(adminDTO.getCellphone())) {
            throw new BadRequestException("El telefono es un campo obligatorio, no puede ser vacío");
        }

        if (ObjectUtils.isEmpty(adminDTO.getPassword()) && validatePassword) {
            throw new BadRequestException("La contraseña es un campo obligatorio, no puede ser vacío");
        }
    }

    private void validateIdentification(Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BadRequestException("La identificación es un campo obligatorio, no puede ser vacio");
        }
    }
}
