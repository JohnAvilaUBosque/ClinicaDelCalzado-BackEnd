package com.ClinicaDelCalzado_BackEnd.dtos.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ServicesStatusEnum {
    RECEIVED("RECIBIDO"),
    FINISHED("TERMINADO"),
    DISPATCHED("DESPACHADO");

    final String value;

    public static String getValue(String keyName) {
        return Arrays.stream(ServicesStatusEnum.values())
                .filter(x -> x.name()
                        .equalsIgnoreCase(keyName))
                .findFirst()
                .map(ServicesStatusEnum::getValue)
                .orElse(StringUtils.EMPTY);
    }
}
