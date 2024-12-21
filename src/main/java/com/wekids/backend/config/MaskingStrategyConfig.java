package com.wekids.backend.config;

import com.wekids.backend.util.masking.strategy.DataMaskingStrategy;
import com.wekids.backend.util.masking.strategy.MaskingStrategy;
import com.wekids.backend.util.masking.strategy.NoMaskingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class MaskingStrategyConfig {

    @Bean
    @Profile({"release"})
    public DataMaskingStrategy noMaskingStrategy() {
        return new NoMaskingStrategy();
    }

    @Bean
    @Profile({"dev", "test"})
    public DataMaskingStrategy maskingStrategy() {
        return new MaskingStrategy();
    }
}
