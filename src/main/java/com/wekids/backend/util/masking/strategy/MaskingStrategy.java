package com.wekids.backend.util.masking.strategy;

import java.math.BigDecimal;

public class MaskingStrategy implements DataMaskingStrategy {
    @Override
    public String mask(String data) {
        if (data == null || data.isEmpty()) return data;

        if (data.matches("^[가-힣]{2,}$")) {
            return data.substring(0, 1) + "*".repeat(data.length() - 1);
        }

        if (data.matches("\\d{12,16}")) {
            return "***********" + data.substring(data.length() - 4);
        }

        return data.replaceAll(".(?=.{4})", "*");
    }

    @Override
    public String maskBalance(BigDecimal balance) {
        if (balance == null) return null;

        String balanceStr = balance.toPlainString();
        int length = balanceStr.length();

        if (length <= 2) return "0";
        return balanceStr.substring(0, 2) + "*".repeat(length - 2);
    }
}