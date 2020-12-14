package com.panchao.blog.aspect;

import lombok.Builder;
import lombok.Data;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName LogAspect
 * @Desc: TODO
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("execution(* com.panchao.blog.controller.*.*(..))")
    public void log() {
    }

    @Before("log()")
    public void doBefore(JoinPoint joinpoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();
        String classMethod = joinpoint.getSignature().getDeclaringTypeName() + "." + joinpoint.getSignature().getName();
        Object[] args = joinpoint.getArgs();
        RequestLog requestLog = RequestLog.builder().url(url).ip(ip).classMethod(classMethod).args(args).build();
        LOGGER.info("Request:{}", requestLog);
    }

    @After("log()")
    public void doAfter() {
        LOGGER.info("----------doAfter-----------");
    }

    @AfterReturning(pointcut = "log()", returning = "result")
    public void doAfterReturn(Object result) {
        LOGGER.info("Result:{}", result);
    }

    @Data
    @Builder
    private static class RequestLog {
        private String url;
        private String ip;
        private String classMethod;
        private Object[] args;
    }
}
