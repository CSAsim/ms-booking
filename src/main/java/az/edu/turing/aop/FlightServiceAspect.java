package az.edu.turing.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class FlightServiceAspect {

    @Pointcut("execution(* az.edu.turing.service.FlightService.*(..))")
    public void flightServiceMethods() {}

    @Before("flightServiceMethods()")
    public void logBeforeMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        log.info("Executing: {} with arguments {}", signature.getMethod().getName(), Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(value = "flightServiceMethods()", returning = "result")
    public void logAfterMethod(JoinPoint joinPoint, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        log.info("Completed: {} with result {}", signature.getMethod().getName(), result);
    }

    @AfterThrowing(value = "flightServiceMethods()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Exception exception) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        log.error("Exception in {}: {}", signature.getMethod().getName(), exception.getMessage(), exception);
    }
}

