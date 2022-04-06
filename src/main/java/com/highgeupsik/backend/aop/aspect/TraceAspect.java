package com.highgeupsik.backend.aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Slf4j
@Aspect
public class TraceAspect {

    @Before("com.highgeupsik.backend.aop.pointcuts.PointCuts.ApiOrServiceOrRepository()")
    public void doTrace(JoinPoint joinPoint) {
        log.info("[trace] {} args = {}", joinPoint.getSignature(), joinPoint.getArgs());
    }
}
