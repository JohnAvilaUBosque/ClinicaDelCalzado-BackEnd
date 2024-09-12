package com.ClinicaDelCalzado_BackEnd.services.impl;

import com.ClinicaDelCalzado_BackEnd.dtos.request.AdminDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.request.UpdateAdminDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.response.AdminDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.response.AdminListDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.enums.AdminStatusEnum;
import com.ClinicaDelCalzado_BackEnd.dtos.enums.AdminTypeEnum;
import com.ClinicaDelCalzado_BackEnd.dtos.userAdmin.AdminDTO;
import com.ClinicaDelCalzado_BackEnd.entity.Administrator;
import com.ClinicaDelCalzado_BackEnd.exceptions.AlreadyExistsException;
import com.ClinicaDelCalzado_BackEnd.exceptions.BadRequestException;
import com.ClinicaDelCalzado_BackEnd.exceptions.ForbiddenException;
import com.ClinicaDelCalzado_BackEnd.repository.userAdmin.IAdministratorRepository;
import com.ClinicaDelCalzado_BackEnd.services.IAdminService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements IAdminService {

    private final IAdministratorRepository administratorRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminServiceImpl(IAdministratorRepository administratorRepository, PasswordEncoder passwordEncoder) {
        this.administratorRepository = administratorRepository;
        this.passwordEncoder = passwordEncoder;
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
                        p.getAdmPhoneNumber(), AdminStatusEnum.getValue(p.getAdminStatus()), p.getHasTemporaryPassword())).get());

        return adminDTOResponse;

    }

    @Override
    public AdminListDTOResponse findAdministratorAll() {

        List<Administrator> administratorList = administratorRepository.findAll();

        AdminListDTOResponse adminListDTOResponse = new AdminListDTOResponse();
        adminListDTOResponse.setAdmins(administratorList.stream().map(p ->
                new AdminDTO(p.getIdAdministrator(), AdminTypeEnum.getValue(p.getRole()), p.getAdminName(),
                        p.getAdmPhoneNumber(), AdminStatusEnum.getValue(p.getAdminStatus()), p.getHasTemporaryPassword())).toList());

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
                passwordEncoder.encode(adminDTO.getPassword()),
                true);

        saveAdmin(administrator);

        return adminDTOResponse("Administrador creado exitosamente.", administrator);
    }

    @Override
    public AdminDTOResponse update(Long adminId, AdminDTORequest adminDTO) {

        adminDTO.setIdentification(adminId);

        validateInputData(adminDTO, false);

        Administrator administratorById = validateIfAdminIdExists(adminId, "El administrador con identificación %s no existe");

        Administrator administrator = buildAdministrator(
                adminId,
                adminDTO.getName(),
                adminDTO.getCellphone(),
                administratorById.getAdminStatus(),
                AdminTypeEnum.getName(adminDTO.getAdminType()),
                administratorById.getPassword(),
                administratorById.getHasTemporaryPassword());

        saveAdmin(administrator);

        return adminDTOResponse("Administrador actualizado exitosamente.", administrator);
    }

    @Override
    public AdminDTOResponse updateStatus(Long adminId, UpdateAdminDTORequest adminDTO) {

        validateIdentification(adminDTO.getAdminId());
        validateIdentification(adminId);

        Administrator administratorPrincipalById = validateIfAdminIdExists(adminDTO.getAdminId(), "El administrador con identificación %s no existe");
        Administrator administratorSecondaryById = validateIfAdminIdExists(adminId, "El administrador con identificación %s no existe");

        if (!administratorPrincipalById.getRole().equalsIgnoreCase(AdminTypeEnum.PRINCIPAL.name())) {
            throw new ForbiddenException(String.format("El administrador con identificación %s no tiene el rol principal para actualizar el estado", adminDTO.getAdminId()));
        }

        if (administratorPrincipalById.getIdAdministrator().equals(administratorSecondaryById.getIdAdministrator())) {
            throw new ForbiddenException("No tiene permiso para cambiar el estado del administrador.");
        }

        Administrator administrator = buildAdministrator(
                administratorSecondaryById.getIdAdministrator(),
                administratorSecondaryById.getAdminName(),
                administratorSecondaryById.getAdmPhoneNumber(),
                AdminStatusEnum.getName(adminDTO.getAdminStatus()),
                administratorSecondaryById.getRole(),
                administratorSecondaryById.getPassword(),
                administratorSecondaryById.getHasTemporaryPassword());

        saveAdmin(administrator);

        return adminDTOResponse("Cambio de estado del Administrador exitosamente.", administrator);
    }

    private Administrator buildAdministrator(Long adminId, String adminName, String adminPhoneNumber,
                                             String adminStatus, String role, String password,
                                             Boolean hasTemporaryPwd) {
        return Administrator.builder()
                .idAdministrator(adminId)
                .adminName(adminName)
                .admPhoneNumber(adminPhoneNumber)
                .adminStatus(adminStatus)
                .role(role)
                .password(password)
                .hasTemporaryPassword(hasTemporaryPwd)
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
                        .hasTemporaryPassword(administrator.getHasTemporaryPassword())
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

    private Administrator validateIfAdminIdExists(Long id, String msg) {
        Optional<Administrator> administratorById = findAdministratorById(id);

        if (administratorById.isEmpty()) {
            throw new NotFoundException(String.format(msg, id));
        }
        return administratorById.get();
    }

    private void validateIdentification(Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BadRequestException("La identificación es un campo obligatorio, no puede ser vacio");
        }
    }

}
