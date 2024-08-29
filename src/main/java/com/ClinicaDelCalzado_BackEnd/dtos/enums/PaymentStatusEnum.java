package com.ClinicaDelCalzado_BackEnd.dtos.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum PaymentStatusEnum {
    PENDING("PENDIENTE"),
    PAID("PAGADO");

    final String value;

    public static String getValue(String keyName) {
        return Arrays.stream(PaymentStatusEnum.values())
                .filter(x -> x.name()
                        .equalsIgnoreCase(keyName))
                .findFirst()
                .map(PaymentStatusEnum::getValue)
                .orElse(StringUtils.EMPTY);
    }
}
