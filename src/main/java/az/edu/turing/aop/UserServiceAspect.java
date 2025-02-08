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
public class UserServiceAspect {

    @Pointcut("execution(* az.edu.turing.service.UserService.*(..))")
    public void userServiceMethods() {}

    @Before("userServiceMethods()")
    public void logBeforeMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        log.info("Executing: {} with arguments {}", signature.getMethod().getName(), Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(value = "userServiceMethods()", returning = "result")
    public void logAfterMethod(JoinPoint joinPoint, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        log.info("Completed: {} with result {}", signature.getMethod().getName(), result);
    }

    @AfterThrowing(value = "userServiceMethods()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Exception exception) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        log.error("Exception in {}: {}", signature.getMethod().getName(), exception.getMessage(), exception);
    }
}

