package com.ClinicaDelCalzado_BackEnd.services.impl;

import com.ClinicaDelCalzado_BackEnd.dtos.request.AdminDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.request.UpdateAdminDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.request.UpdateAdminPasswordDTO;
import com.ClinicaDelCalzado_BackEnd.dtos.response.AdminDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.response.AdminListDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.enums.AdminStatusEnum;
import com.ClinicaDelCalzado_BackEnd.dtos.enums.AdminTypeEnum;
import com.ClinicaDelCalzado_BackEnd.dtos.response.UpdateAdminPasswordDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.userAdmin.AdminDTO;
import com.ClinicaDelCalzado_BackEnd.entity.Administrator;
import com.ClinicaDelCalzado_BackEnd.exceptions.AlreadyExistsException;
import com.ClinicaDelCalzado_BackEnd.exceptions.BadRequestException;
import com.ClinicaDelCalzado_BackEnd.exceptions.UnauthorizedException;
import com.ClinicaDelCalzado_BackEnd.repository.userAdmin.IAdministratorRepository;
import com.ClinicaDelCalzado_BackEnd.services.IAdminService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Objects;
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
    public AdminDTOResponse update(Long adminId, UpdateAdminDTORequest adminDTO) {

        validateInputData(AdminDTORequest.builder()
                .identification(adminId)
                .build(), false);

        Administrator administrator = matchDifferencesAdmin(validateIfAdminIdExists(adminId), adminDTO);

        saveAdmin(administrator);

        return adminDTOResponse("Administrador actualizado exitosamente.", administrator);
    }

    @Override
    public UpdateAdminPasswordDTOResponse updatePassword(Long adminId, UpdateAdminPasswordDTO adminDTO) {

        validateIdentification(adminId);

        Administrator administrator = validateIfAdminIdExists(adminId);

        String pwdNewEncode = passwordEncoder.encode(adminDTO.getNewPassword());

        validatePassword(adminDTO.getOldPassword(), adminDTO.getNewPassword(), adminDTO.getConfirmNewPassword());

        if (!passwordEncoder.matches(adminDTO.getOldPassword(), administrator.getPassword())) {
            throw new UnauthorizedException("Clave anterior es incorrecta.");
        }

        if (!adminDTO.getNewPassword().equals(adminDTO.getConfirmNewPassword())) {
            throw new BadRequestException("Datos proporcionados son inválidos o las claves nuevas no coinciden.");
        }

        if (adminDTO.getOldPassword().equals(adminDTO.getNewPassword())) {
            throw new BadRequestException("La clave anterior y la nueva no pueden ser iguales.");
        }

        administrator.setPassword(pwdNewEncode);
        administrator.setHasTemporaryPassword(false);

        saveAdmin(administrator);

        return UpdateAdminPasswordDTOResponse.builder()
                .message("Clave cambiada exitosamente.")
                .build();
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

    private void validateInputData(AdminDTORequest adminDTO, Boolean validateAllFields) {

        validateIdentification(adminDTO.getIdentification());

        if (ObjectUtils.isEmpty(adminDTO.getName()) && validateAllFields) {
            throw new BadRequestException("El nombre es un campo obligatorio, no puede ser vacío");
        }

        if (ObjectUtils.isEmpty(adminDTO.getAdminType()) && validateAllFields) {
            throw new BadRequestException("El tipo de administrador es un campo obligatorio, no puede ser vacío");
        }

        if (ObjectUtils.isEmpty(adminDTO.getCellphone()) && validateAllFields) {
            throw new BadRequestException("El telefono es un campo obligatorio, no puede ser vacío");
        }

        if (ObjectUtils.isEmpty(adminDTO.getPassword()) && validateAllFields) {
            throw new BadRequestException("La contraseña es un campo obligatorio, no puede ser vacío");
        }
    }

    private Administrator validateIfAdminIdExists(Long id) {
        Optional<Administrator> administratorById = findAdministratorById(id);

        if (administratorById.isEmpty()) {
            throw new NotFoundException(String.format("El administrador con identificación %s no existe", id));
        }
        return administratorById.get();
    }

    private void validateIdentification(Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BadRequestException("La identificación es un campo obligatorio, no puede ser vacio");
        }
    }

    private void validatePassword(String oldPassword, String newPassword, String confirmNewPassword) {
        if (ObjectUtils.isEmpty(oldPassword)) {
            throw new BadRequestException("La contraseña actual no puede ser vacia");
        }

        if (ObjectUtils.isEmpty(newPassword)) {
            throw new BadRequestException("La nueva contraseña no puede ser vacia");
        }

        if (ObjectUtils.isEmpty(confirmNewPassword)) {
            throw new BadRequestException("La confirmación de la nueva contraseña no puede ser vacia");
        }
    }

    private Administrator matchDifferencesAdmin(Administrator currentDataAdmin, UpdateAdminDTORequest newDataAdmin) {
        return buildAdministrator(
                currentDataAdmin.getIdAdministrator(),
                ObjectUtils.isEmpty(newDataAdmin.getName()) || Objects.equals(currentDataAdmin.getAdminName(), newDataAdmin.getName()) ?
                        currentDataAdmin.getAdminName() :
                        newDataAdmin.getName(),
                ObjectUtils.isEmpty(newDataAdmin.getCellphone()) || Objects.equals(currentDataAdmin.getAdmPhoneNumber(), newDataAdmin.getCellphone()) ?
                        currentDataAdmin.getAdmPhoneNumber() :
                        newDataAdmin.getCellphone(),
                ObjectUtils.isEmpty(newDataAdmin.getAdminStatus()) || Objects.equals(AdminStatusEnum.getValue(currentDataAdmin.getAdminStatus()), newDataAdmin.getAdminStatus()) ?
                        currentDataAdmin.getAdminStatus() :
                        AdminStatusEnum.getName(newDataAdmin.getAdminStatus()),
                ObjectUtils.isEmpty(newDataAdmin.getAdminType()) || Objects.equals(AdminTypeEnum.getValue(currentDataAdmin.getRole()), newDataAdmin.getAdminType()) ?
                        currentDataAdmin.getRole() :
                        AdminTypeEnum.getName(newDataAdmin.getAdminType()),
                currentDataAdmin.getPassword(),
                currentDataAdmin.getHasTemporaryPassword());
    }

}
