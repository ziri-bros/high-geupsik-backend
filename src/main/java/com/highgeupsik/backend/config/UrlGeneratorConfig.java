package com.highgeupsik.backend.config;

import com.highgeupsik.backend.utils.UrlGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UrlGeneratorConfig {

    @Bean
    public UrlGenerator urlGenerator() {
        return new UrlGenerator();
    }
}
