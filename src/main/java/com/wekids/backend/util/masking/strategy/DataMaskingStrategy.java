package com.wekids.backend.util.masking.strategy;

import java.math.BigDecimal;

public interface DataMaskingStrategy {
    String mask(String data);
    String maskBalance(BigDecimal balance);
}
