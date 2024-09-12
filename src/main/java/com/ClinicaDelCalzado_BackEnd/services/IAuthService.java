package com.ClinicaDelCalzado_BackEnd.services;

import com.ClinicaDelCalzado_BackEnd.dtos.login.LoginDTO;
import com.ClinicaDelCalzado_BackEnd.dtos.response.AuthDTOResponse;

public interface IAuthService {

    AuthDTOResponse login(LoginDTO loginDTO);
}
