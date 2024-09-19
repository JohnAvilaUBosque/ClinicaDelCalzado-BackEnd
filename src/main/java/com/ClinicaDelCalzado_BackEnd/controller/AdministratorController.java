package com.ClinicaDelCalzado_BackEnd.controller;

import com.ClinicaDelCalzado_BackEnd.dtos.request.AdminDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.request.UpdateAdminDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.request.UpdateAdminPasswordDTO;
import com.ClinicaDelCalzado_BackEnd.dtos.request.UpdateAdminQuestionDTO;
import com.ClinicaDelCalzado_BackEnd.dtos.response.AdminDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.response.AdminListDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.response.UpdateAdminPasswordDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.response.UpdateAdminQuestionDTOResponse;
import com.ClinicaDelCalzado_BackEnd.services.IAdminService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admins")
public class AdministratorController {

    @Autowired
    private IAdminService adminService;

    private ModelMapper modelMapper;

    @PostMapping("/created")
    public ResponseEntity<AdminDTOResponse> createAdministrator(@RequestBody AdminDTORequest adminDTORequest) {

        AdminDTOResponse responseDTO = adminService.create(adminDTORequest);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/updated/{adminId}")
    public ResponseEntity<AdminDTOResponse> updateAdministrator(
            @PathVariable Long adminId,
            @RequestBody UpdateAdminDTORequest adminDTORequest) {

        AdminDTOResponse responseDTO = adminService.update(adminId, adminDTORequest);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<AdminListDTOResponse> getAllAdministrator() {

        AdminListDTOResponse responseDTO = adminService.findAdministratorAll();
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/{adminId}")
    public ResponseEntity<AdminDTOResponse> getAdministratorById(@PathVariable Long adminId) {

        AdminDTOResponse responseDTO = adminService.findAdministratorByIdAdmin(adminId);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("/password/{adminId}")
    public ResponseEntity<UpdateAdminPasswordDTOResponse> updatePasswordAdministrator(
            @PathVariable Long adminId,
            @RequestBody UpdateAdminPasswordDTO adminDTORequest) {

        UpdateAdminPasswordDTOResponse responseDTO = adminService.updatePassword(adminId, adminDTORequest);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("/edit-personal-information/{adminId}")
    public ResponseEntity<UpdateAdminQuestionDTOResponse> updatePersonalInformation(
            @PathVariable Long adminId,
            @RequestBody UpdateAdminQuestionDTO adminDTORequest) {

        UpdateAdminQuestionDTOResponse responseDTO = adminService.updateAnswer(adminId, adminDTORequest);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

}
