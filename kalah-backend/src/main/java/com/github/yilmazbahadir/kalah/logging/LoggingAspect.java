package com.github.yilmazbahadir.kalah.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * <h1>This class is responsible for logging</h1>
 *
 * Logs each method call if trace is enabled.
 * Logs each exception in the system.
 *
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    /**
     * Advice that logs every rest request handled by RestControllers
     *
     * @param joinPoint join point for advice
     * */
    @Around("execution(* com.github.yilmazbahadir.kalah.service.*.*(..))") // TODO controllers ?
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object value = null;
        try {
            value = joinPoint.proceed();
        } catch (Throwable throwable) {
            throw throwable;
        } finally {
            log.trace("### < Method {}.{}({}) is invoked and returned {} >",
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), joinPoint.getArgs(), value);
        }
        return value;
    }

    /**
     * Advice that logs methods throwing exceptions.
     *
     * @param joinPoint join point for advice
     * @param e exception
     */
    @AfterThrowing(value = "within(com.github.yilmazbahadir.kalah..*)", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error("Exception in {}.{}() with cause = \'{}\' and exception = \'{}\'",
                joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),
                e.getCause() != null? e.getCause() : "NULL", e.getMessage());
    }

}
