package com.jinjin.jintranet.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {
    @Pointcut("execution(* com.jinjin.jintranet.*.service..*.*(..))")
    public void serviceLog() {}

    //클래스 이름 패턴이 *Controller
    @Pointcut("execution(* *..*Controller.*(..))")
    public void controllerLog() {}
}
