package com.jinjin.jintranet.aop;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Throw;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class TraceAspect {
    @Before("@annotation(com.jinjin.jintranet.aop.Trace)")
    public void doTrace(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        log.info("[trace] {} args={}" , joinPoint.getSignature() , args);
    }

    @Around(("@annotation(retry)"))
    public Object retry(ProceedingJoinPoint joinPoint , Retry retry) throws Throwable { //api에서 간헐적으로 발생할때 , @Retry annotation 을 붙여서 사용
        log.info("[retry] {} retry = {}" , joinPoint.getSignature() , retry);

        int maxRetry = retry.value();
        Exception exceptionHolder = null;

        for(int retryCount = 1 ; retryCount < maxRetry ; retryCount++) {
            try {
                log.info("[retry] {} try count = {}/{}" , retryCount , maxRetry);
                joinPoint.proceed();
            } catch (Exception e) {
                exceptionHolder = e;
            }
        }
        throw exceptionHolder;
    }
}
