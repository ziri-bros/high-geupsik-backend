package com.highgeupsik.backend.aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class RetryAspect {

    @Around("com.highgeupsik.backend.aop.pointcuts.PointCuts.ApiOrServiceOrRepository()")
    public Object doRetry(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[retry] {} args = {}", joinPoint.getSignature(), joinPoint.getArgs());
        int maxRetry = 3;
        Exception exceptionHolder = null;
        for (int retryCount = 1; retryCount <= maxRetry; retryCount++) {
            try {
                log.info("[retry] try count = {}/{}", retryCount, maxRetry);
                return joinPoint.proceed();
            } catch (Exception e) {
                exceptionHolder = e;
            }
        }
        throw exceptionHolder;
    }
}
