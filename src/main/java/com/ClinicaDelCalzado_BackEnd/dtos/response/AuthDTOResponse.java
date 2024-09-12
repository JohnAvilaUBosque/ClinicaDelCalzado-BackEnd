package com.ClinicaDelCalzado_BackEnd.dtos.response;

import com.ClinicaDelCalzado_BackEnd.util.SecurityConstants;
import lombok.Data;

@Data
public class AuthDTOResponse {

    private String accessToken;
    private String tokenType = SecurityConstants.PREFIX;

    public AuthDTOResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
