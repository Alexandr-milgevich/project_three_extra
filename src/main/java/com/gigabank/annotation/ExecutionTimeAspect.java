package com.gigabank.annotation;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;

/**
 * Аспект для логирования времени выполнения методов, помеченных аннотацией {@link LogExecutionTime}.
 * Измеряет время выполнения и логирует начало/окончание работы метода.
 *
 * @see LogExecutionTime
 */
@Slf4j
@Aspect
@Service
public class ExecutionTimeAspect {
    /**
     * Логирует время выполнения метода.
     *
     * @param joinPoint точка соединения AOP
     * @return результат оригинального метода
     */
    @Around("@annotation(com.gigabank.annotation.LogExecutionTime)")
    public Object logExecutionTime(@NotNull ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        long startTime = System.currentTimeMillis();

        log.info("{} started", methodName);

        try {
            Object proceed = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            log.info("{} finished, duration: {} ms", methodName, duration);
            return proceed;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("ExecutionTimeAspect stop method {}. Failed after {} ms. ERROR: {}", methodName, duration, e.getMessage());
            throw e;
        }
    }
}