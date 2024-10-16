package com.ClinicaDelCalzado_BackEnd.dtos.response;

import com.ClinicaDelCalzado_BackEnd.util.SecurityConstants;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthDTOResponse {

    private String message;
    private String accessToken;
    private String tokenType = SecurityConstants.PREFIX;
    private Boolean hasTemporaryPassword;

    public AuthDTOResponse(String message, String accessToken, Boolean hasTemporaryPassword) {
        this.message = message;
        this.accessToken = accessToken;
        this.hasTemporaryPassword = hasTemporaryPassword;
    }
}
