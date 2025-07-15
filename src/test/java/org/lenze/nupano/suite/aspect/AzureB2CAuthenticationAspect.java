package org.lenze.nupano.suite.aspect;

import io.cucumber.java.en.When;
import net.serenitybdd.core.Serenity;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.lenze.nupano.suite.annotations.AzureB2CAuthentication;
import org.lenze.nupano.suite.authentiction.oauth2.authorizationcode.pkce.Azure;

import java.lang.reflect.Method;

@Aspect
public class AzureB2CAuthenticationAspect {
    @Pointcut("@annotation(io.cucumber.java.en.When)")
    public void whenAnnotatedMethod() {}

    @Pointcut("@annotation(org.lenze.nupano.suite.annotations.AzureB2CAuthentication)")
    public void azureB2CAuthenticationAnnotatedMethod() {}

    @Before("whenAnnotatedMethod() && args(..)")
    public void beforeWhenMethod(JoinPoint joinPoint) {
        Method method = getMethodFromJoinPoint(joinPoint);

        if (method != null) {
            When when = method.getAnnotation(When.class);

            if (when.value().contains("authenticated through")) {
                Object[] args = joinPoint.getArgs();
                String user = (String) args[1];
                String organization = (String) args[2];

                Serenity.environmentVariables().setProperty("nupanosuite_user", user.concat("@").concat(organization).concat(".com"));
            }
        }
    }

    @Before("azureB2CAuthenticationAnnotatedMethod()")
    public void beforeAzureB2CAuthenticationMethod(JoinPoint joinPoint) {
        Method method = getMethodFromJoinPoint(joinPoint);

        if (method != null) {
            AzureB2CAuthentication azureB2CAuthentication = method.getAnnotation(AzureB2CAuthentication.class);
            if (azureB2CAuthentication.type().equalsIgnoreCase("pkce"))
                new Azure().B2C();
        }
    }

    private Method getMethodFromJoinPoint(JoinPoint joinPoint) {
        try {
            String methodName = joinPoint.getSignature().getName();
            Class<?>[] parameterTypes = ((org.aspectj.lang.reflect.MethodSignature) joinPoint.getSignature()).getMethod().getParameterTypes();
            return joinPoint.getTarget().getClass().getMethod(methodName, parameterTypes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
