package com.github.chenlijia1111.utils.core;

import com.github.chenlijia1111.utils.common.Result;
import com.github.chenlijia1111.utils.core.annos.PropertyCheck;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

/**
 * 参数校验 aop
 *
 * @author 陈礼佳
 * @since 2019/12/22 14:25
 */
@Aspect
@Component
public class ParameterCheckProxy {


    @Pointcut(value = "@annotation(com.github.chenlijia1111.utils.core.annos.PropertyCheck)")
    public void pointCut() {
    }

    @Around(value = "pointCut()")
    public Object before(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        //代理目标对象
        Object target = joinPoint.getTarget();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();


        //判断类有没有注解
        PropertyCheck classAnnotation = target.getClass().getAnnotation(PropertyCheck.class);
        if (Objects.isNull(classAnnotation)) {
            //判断方法有没有校验注解
            PropertyCheck methodAnnotation = method.getAnnotation(PropertyCheck.class);
            Parameter[] parameters = method.getParameters();
            if (Objects.isNull(methodAnnotation)) {
                //判断参数
                if (null != parameters && parameters.length > 0) {
                    for (int i = 0; i < parameters.length; i++) {
                        Parameter parameter = parameters[i];
                        //判断参数有没有带校验注解
                        PropertyCheck parameterAnnotation = parameter.getAnnotation(PropertyCheck.class);
                        if (Objects.nonNull(parameterAnnotation)) {
                            //参数需要校验
                            if (Objects.isNull(args[i])) {
                                return Result.failure("参数不合法");
                            }
                        }
                        //判断是否是基础对象,如果不是基础对象,判断这个类里面的属性有没有校验注解,是否需要校验
                        Result result = PropertyCheckUtil.checkProperty(args[i]);
                        if (!result.getSuccess()) {
                            return result;
                        }
                    }
                }

            } else {
                //对所有参数进行校验
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    if (Objects.isNull(arg)) {
                        return Result.failure("参数不合法");
                    }
                    Result result = PropertyCheckUtil.checkProperty(arg);
                    if (!result.getSuccess()) {
                        return result;
                    }
                }
            }

        } else {
            //直接每个参数都进行校验
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (Objects.isNull(arg)) {
                    return Result.failure("参数不合法");
                }
                Result result = PropertyCheckUtil.checkProperty(arg);
                if (!result.getSuccess()) {
                    return result;
                }
            }
        }

        Object proceed = joinPoint.proceed(args);
        return proceed;
    }

}
