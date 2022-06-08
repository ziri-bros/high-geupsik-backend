package com.highgeupsik.backend.config;

import com.highgeupsik.backend.aop.aspect.RetryAspect;
import com.highgeupsik.backend.aop.aspect.TraceAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class AopConfig {

    @Bean
    public TraceAspect traceAspect() {
        return new TraceAspect();
    }

    @Bean
    public RetryAspect retryAspect() {
        return new RetryAspect();
    }
}
