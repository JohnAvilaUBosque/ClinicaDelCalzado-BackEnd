package com.ClinicaDelCalzado_BackEnd.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Encrypt {

    public static String encode(String s) {
        String response = "error";
        response = new String(Base64.getEncoder().encode(s.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        return response;
    }

    public static String decode(String s) {
        String response = "error";
        response = new String(Base64.getDecoder().decode(s.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        return response;
    }
}
