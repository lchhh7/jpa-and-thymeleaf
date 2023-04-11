package com.jinjin.jintranet.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Slf4j
public class ServiceAspect {

    @Component
    @Aspect
    @Order(2)
    public static class serviceLogAspect {
        @Around("com.jinjin.jintranet.aop.Pointcuts.serviceLog()")
        public Object asLog(ProceedingJoinPoint joinPoint) throws Throwable{
            //실행되는 함수 이름을 가져오고 출력
            log.info("[log] {} " , joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }

    @Component
    @Aspect
    @Order(1)
    public static class controllerLogAspect {
        @Around("com.jinjin.jintranet.aop.Pointcuts.controllerLog()")
        public Object asLog(ProceedingJoinPoint joinPoint) throws Throwable{
            log.info("[controllerLog] {} " , joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }

    //@Around -> joinPoint.proceed() 호출하지 않으면 타겟을 호출하지 않아서 다음로직이 실행되지 않음
    //@Around = @Before(시작전) + @after(메소드 종료시(finally) + @AfterReturning(메소드 정상반환) + @AfterThrowing(메서드 예외)
    //@Around 제외한 어드바이스가 존재하는 이유 ?? ->  타켓의 반환값이 void이면 return할수가 없음 -> 다음로직이 실행되지 않음
    // 따라서 @Around 제외한 다은 어드바이스를 실행해야 하는 경우도 많고, 애초에 제한이 있는 개발이 가시성이 좋고 확실함
}