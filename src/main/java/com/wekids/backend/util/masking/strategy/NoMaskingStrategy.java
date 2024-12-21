package com.wekids.backend.util.masking.strategy;

import java.math.BigDecimal;

public class NoMaskingStrategy implements DataMaskingStrategy {
    @Override
    public String mask(String data) {
        return data;
    }

    @Override
    public String maskBalance(BigDecimal balance) {
        return balance.toString();
    }
}
