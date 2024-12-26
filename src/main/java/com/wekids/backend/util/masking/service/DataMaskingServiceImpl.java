package com.wekids.backend.util.masking.service;

import com.wekids.backend.util.masking.strategy.DataMaskingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DataMaskingServiceImpl implements DataMaskingService{
    private final DataMaskingStrategy maskingStrategy;

    @Override
    public String maskData(String data) {
        return maskingStrategy.mask(data);
    }

    @Override
    public String maskBalance(BigDecimal balance) {
        return maskingStrategy.maskBalance(balance);
    }
}