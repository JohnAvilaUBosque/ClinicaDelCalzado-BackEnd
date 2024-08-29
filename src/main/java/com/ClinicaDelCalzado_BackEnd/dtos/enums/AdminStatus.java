package com.ClinicaDelCalzado_BackEnd.dtos.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum AdminStatus {
    ACTIVE("ACTIVO"),
    INACTIVE("INACTIVO");

    final String value;

    public static String getValue(String keyName) {
        return Arrays.stream(AdminStatus.values())
                .filter(x -> x.name()
                        .equalsIgnoreCase(keyName))
                .findFirst()
                .map(AdminStatus::getValue)
                .orElse(StringUtils.EMPTY);
    }
}
