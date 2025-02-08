package az.edu.turing.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class BookingServiceAspect {

    @Pointcut("execution(* az.edu.turing.service.BookingService.*(..))")
    public void bookingServiceMethods() {
    }

    @Before("bookingServiceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        log.info("Executing method: {} with arguments: {}", signature.getName(), Arrays.toString(joinPoint.getArgs()));
    }

    @Around("bookingServiceMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;
            log.info("Method {} executed in {} ms", joinPoint.getSignature().getName(), executionTime);
            return result;
        } catch (Throwable ex) {
            log.error("Exception in method {}: {}", joinPoint.getSignature().getName(), ex.getMessage(), ex);
            throw ex;
        }
    }

    @AfterReturning(value = "bookingServiceMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("Method {} returned: {}", joinPoint.getSignature().getName(), result);
    }

    @AfterThrowing(value = "bookingServiceMethods()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Exception exception) {
        log.error("Exception in method {}: {}", joinPoint.getSignature().getName(), exception.getMessage(), exception);
    }
}

