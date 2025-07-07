package com.jyx.healthsys.aspect;

import com.jyx.config.JwtConfig;

import com.jyx.healthsys.annotation.LogOperation;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.jyx.healthsys.entity.Log;
import com.jyx.healthsys.mapper.LogMapper;

import io.jsonwebtoken.Claims;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.JoinPoint;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.ProceedingJoinPoint;

import com.baomidou.mybatisplus.core.mapper.BaseMapper; // MyBatis-Plus
import lombok.Data; // 如果用了 Lombok
import org.apache.ibatis.annotations.Mapper; // Mapper注解
@Aspect
@Component
public class LogAspect {

    @Autowired
    private LogMapper LogMapper;

    @Pointcut("execution(* com.jyx.healthsys.controller.UserController.*(..))")
    public void logPointcut() {
    }

    @Before("logPointcut()")
    public void saveLog(JoinPoint joinPoint) throws NoSuchFieldException, IllegalAccessException {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String ip = request.getRemoteAddr();
        String url = request.getRequestURI();
        String params = Arrays.toString(joinPoint.getArgs());

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String content = "默认操作"; // 默认值
        LogOperation logOperation = method.getAnnotation(LogOperation.class);
        if (logOperation != null) {
            content = logOperation.value();  // 解析自定义描述
        }


        // 1️⃣ 从请求头取 token
        String token = request.getHeader("X-Token");
        String username = "anonymous"; // 默认

        if (token != null) {
            try {
                // 这里还是用你自己的 JWT 工具解析
                Claims claims = JwtConfig.parseToken(token);
                username = claims.get("username", String.class);
            } catch (Exception e) {
                System.out.println("Token解析失败: " + e.getMessage());
            }
        }
        else if(Objects.equals(url, "/user/login"))
        {
            Object[] args = joinPoint.getArgs(); // 获取方法参数
            for (Object arg : args) {
                if (arg == null) continue;

                Class<?> clazz = arg.getClass();
                if (clazz.getSimpleName().equals("User")) { // 或者 arg instanceof User
                    Field field = null;
                    field = clazz.getDeclaredField("username");
                    field.setAccessible(true);
                    username = field.get(arg).toString();
                }
            }
        }
        //格式化时间
        LocalDate today = LocalDate.now();
        String dateStr = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));


        Log log = new Log();
        log.setUsername(username);
        log.setContent(content);
        log.setDate(dateStr);

        LogMapper.insert(log);
    }
}
