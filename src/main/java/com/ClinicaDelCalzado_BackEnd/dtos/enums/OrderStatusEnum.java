package com.ClinicaDelCalzado_BackEnd.dtos.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum OrderStatusEnum {
    VALID("VIGENTE"),
    CANCELED("CANCELADA");

    final String value;

    public static String getValue(String keyName) {
        return Arrays.stream(OrderStatusEnum.values())
                .filter(x -> x.name()
                        .equalsIgnoreCase(keyName))
                .findFirst()
                .map(OrderStatusEnum::getValue)
                .orElse(StringUtils.EMPTY);
    }
}
