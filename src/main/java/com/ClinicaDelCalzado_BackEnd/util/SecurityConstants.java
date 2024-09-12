package com.ClinicaDelCalzado_BackEnd.util;

public class SecurityConstants {

    /**
     * Constante para determinar la validez del token por un lapso de 5 minutos (milisegundos)
     */
    public static final long JWT_EXPIRATION_TOKEN = 300000;
    public static final String JWT_SIGNATURE = "SIGNATURE";
    public static final String HEADER = "Authorization";
    public static final String PREFIX = "Bearer ";

}
