package com.ClinicaDelCalzado_BackEnd.controller;

import com.ClinicaDelCalzado_BackEnd.dtos.Request.AdminDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.Request.WorkOrderDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.Response.AdminDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.Response.WorkOrderDTOResponse;
import com.ClinicaDelCalzado_BackEnd.entity.Administrator;
import com.ClinicaDelCalzado_BackEnd.services.IAdminService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admins")
public class AdministratorController {

    @Autowired
    private IAdminService adminService;

    private ModelMapper modelMapper;

    @PostMapping("/created")
    public ResponseEntity<AdminDTOResponse> createWorkOrder(@RequestBody AdminDTORequest adminDTORequest) {

        AdminDTOResponse responseDTO = adminService.create(adminDTORequest);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

}
