package com.highgeupsik.backend.config;

import com.baechu.cache.datastructure.MyCacheStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyCacheConfig {

    @Bean
    public MyCacheStore cacheStore() {
        return new MyCacheStore();
    }
}
