package com.highgeupsik.backend.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import repository.CacheStore;
import repository.CacheStoreImpl;
import service.CacheStoreService;
import service.CacheStoreServiceImpl;


@Configuration
public class CacheConfig {

    @Bean
    public CacheStore cacheStore() {
        return new CacheStoreImpl();
    }

    @Bean
    public CacheStoreService cacheStoreService() {
        return new CacheStoreServiceImpl(cacheStore());
    }
}
