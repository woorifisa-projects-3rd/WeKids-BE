package com.wekids.backend.util.masking.service;

import java.math.BigDecimal;

public interface DataMaskingService {
    public String maskData(String data);
    public String maskBalance(BigDecimal balance);
}
