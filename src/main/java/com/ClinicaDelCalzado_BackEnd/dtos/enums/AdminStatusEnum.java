package com.ClinicaDelCalzado_BackEnd.dtos.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum AdminStatusEnum {
    ACTIVE("ACTIVE", "ACTIVO"),
    INACTIVE("INACTIVE","INACTIVO");


    final String keyName;
    final String value;

    public static String getValue(String keyName) {
        return Arrays.stream(AdminStatusEnum.values())
                .filter(x -> x.getKeyName()
                        .equalsIgnoreCase(keyName))
                .findFirst()
                .map(AdminStatusEnum::getValue)
                .orElse(StringUtils.EMPTY);
    }

    public static String getName(String keyValue) {
        return Arrays.stream(AdminStatusEnum.values())
                .filter(x -> x.getValue()
                        .equalsIgnoreCase(keyValue))
                .findFirst()
                .map(AdminStatusEnum::getKeyName)
                .orElse(StringUtils.EMPTY);
    }
}
