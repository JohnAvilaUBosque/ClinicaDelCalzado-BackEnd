package com.ClinicaDelCalzado_BackEnd.services.impl;

import com.ClinicaDelCalzado_BackEnd.dtos.login.LoginDTO;
import com.ClinicaDelCalzado_BackEnd.dtos.response.AuthDTOResponse;
import com.ClinicaDelCalzado_BackEnd.services.IAuthService;
import com.ClinicaDelCalzado_BackEnd.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtGenerator;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, JWTUtil jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
    }

    @Override
    public AuthDTOResponse login(LoginDTO loginDTO) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDTO.getIdentification(), loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);

        return new AuthDTOResponse("Inicio de sesión exitoso!", token);

    }


}
