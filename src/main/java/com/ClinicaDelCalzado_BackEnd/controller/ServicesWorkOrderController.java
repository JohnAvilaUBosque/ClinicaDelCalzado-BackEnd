package com.ClinicaDelCalzado_BackEnd.controller;

import com.ClinicaDelCalzado_BackEnd.dtos.response.ServicesDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.response.ServicesListDTOResponse;
import com.ClinicaDelCalzado_BackEnd.services.IProductService;
import com.ClinicaDelCalzado_BackEnd.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/services-work-orders")
public class ServicesWorkOrderController {

    @Autowired
    private IProductService productService;

    private JWTUtil jwtUtil;

    @GetMapping("/list")
    public ResponseEntity<ServicesListDTOResponse> getAllServices(Authentication authentication) {

        ServicesListDTOResponse responseDTO = productService.findServicesAll();
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/{serviceId}")
    public ResponseEntity<ServicesDTOResponse> getServicesById(@PathVariable Integer serviceId, Authentication authentication) {

        ServicesDTOResponse responseDTO = productService.findServicesById(serviceId);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
