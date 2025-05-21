package gigabank.accountmanagement.annotations;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Аспект для логирования времени выполнения методов, помеченных аннотацией {@link LogExecutionTime}.
 * Измеряет время выполнения и логирует начало/окончание работы метода.
 *
 * @see LogExecutionTime
 */
@Aspect
@Service
public class ExecutionTimeAspect {
    private static final Logger log = LoggerFactory.getLogger(ExecutionTimeAspect.class);

    /**
     * Логирует время выполнения метода.
     *
     * @param joinPoint точка соединения AOP
     * @return результат оригинального метода
     * @throws Throwable в случае ошибок выполнения
     */
    @Around("@annotation(gigabank.accountmanagement.annotations.LogExecutionTime)")
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
