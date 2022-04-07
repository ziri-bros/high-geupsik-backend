package com.highgeupsik.backend.aop.pointcuts;

import org.aspectj.lang.annotation.Pointcut;

public class PointCuts {

    @Pointcut("execution(* com.highgeupsik.backend.api..*(..))")
    public void allApi() {
    }

    @Pointcut("execution(* com.highgeupsik.backend.service..*(..))")
    public void allService() {
    }

    @Pointcut("execution(* com.highgeupsik.backend.repository..*(..))")
    public void allRepository() {
    }

    @Pointcut("allApi() || allService() || allRepository()")
    public void ApiOrServiceOrRepository() {
    }
}
