package com.github.chenlijia1111.utils.core;

import com.github.chenlijia1111.utils.common.Result;
import com.github.chenlijia1111.utils.core.annos.PropertyCheck;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

/**
 * 参数校验 aop
 * 使用方式
 * 使用@Bean 注解注入spring 即可
 * 然后在类上使用 {@link PropertyCheck} 注解,那么这个类的所有方法都会被拦截到,默认校验所有参数
 * 或者在方法上使用 {@link PropertyCheck} 注解,那么这个方法会被拦截到,默认不校验所有参数,
 * 表示要特殊对待,每个要校验的参数需要单独带上注解
 * <p>
 * 然后会迭代每个参数,只有参数里的字段被 {@link PropertyCheck} 注解修饰,才会进行处理
 * 如果是基本类型参数,需要在参数之前加上 {@link PropertyCheck} 注解修饰,就会对其做非空判断(前提是这个方法或者类要被拦截),不加就不会做校验
 * <p>
 * 如何防止查询方法被校验参数 ? 一般查询方法参数都是封装为对象来提供外界查询,如果不想要校验的字段就不要加校验注解即可
 * 如果没有封装,都是一个一个的基本类型,那就在方法上加上校验注解,标识要特殊对待,没有注解的参数就不会校验了
 */
@Aspect
public class ParameterCheckProxy {


    //表示在类上有这个注解或者在方法上有这个注解都会进行拦截
    @Pointcut(value = "@within(com.github.chenlijia1111.utils.core.annos.PropertyCheck) || " +
            "@annotation(com.github.chenlijia1111.utils.core.annos.PropertyCheck)")
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
        //判断方法有没有校验注解
        PropertyCheck methodAnnotation = method.getAnnotation(PropertyCheck.class);
        Parameter[] parameters = method.getParameters();
        if (Objects.isNull(classAnnotation)) {
            if (Objects.nonNull(methodAnnotation)) {
                //方法有注解(表示特殊对待,每个参数需要单独带上注解才会进行校验)
                //判断参数
                Result result = specialCheckMethod(parameters, args);
                return result;

            }
            //类没有注解,方法也没有注解,进不来

        } else {

            if (Objects.nonNull(methodAnnotation)) {
                //方法有注解(表示特殊对待,每个参数需要单独带上注解才会进行校验)
                //判断参数
                Result result = specialCheckMethod(parameters, args);
                return result;
            } else {
                //在类上有注解(导致对每一个方法都切中了)
                //直接每个参数都进行校验
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    Result result = PropertyCheckUtil.checkProperty(arg);
                    if (!result.getSuccess()) {
                        return result;
                    }
                }
            }

        }

        Object proceed = joinPoint.proceed(args);
        return proceed;
    }


    /**
     * 在方法有校验注解,需要特殊处理,只有参数有注解的参数才需要进行校验
     *
     * @param parameters 参数类型
     * @param args       参数值
     * @return
     */
    public Result specialCheckMethod(Parameter[] parameters, Object[] args) {
        if (null != parameters && parameters.length > 0) {
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                //判断参数有没有带校验注解
                PropertyCheck parameterAnnotation = parameter.getAnnotation(PropertyCheck.class);
                if (Objects.nonNull(parameterAnnotation)) {
                    //参数需要校验
                    if (!parameterAnnotation.ignoreNull() && Objects.isNull(args[i])) {
                        return Result.failure("参数" + parameterAnnotation.name() + "不合法");
                    }
                    //判断需要校验的属性
                    Result result = PropertyCheckUtil.checkProperty(args[i]);
                    if (!result.getSuccess()) {
                        return result;
                    }
                }

            }
        }
        return Result.success("检测通过");
    }


}
