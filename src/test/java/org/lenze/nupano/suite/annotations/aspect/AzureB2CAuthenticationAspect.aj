package org.lenze.nupano.suite.annotations.aspect;

import org.aspectj.lang.JoinPoint;
import org.lenze.nupano.suite.annotations.AzureB2CAuthentication;
import org.lenze.nupano.suite.authentiction.oauth2.authorizationcode.pkce.Azure;
import org.lenze.nupano.suite.enummeration.AzureB2CAuthenticationType;
import org.lenze.nupano.suite.helper.SuiteProperties;
import org.lenze.nupano.suite.stepdefinitions.ui.Authentication;
import io.cucumber.java.en.When;
import net.serenitybdd.core.Serenity;

import java.lang.reflect.Method;

public privileged aspect AzureB2CAuthenticationAspect {

    pointcut whenAnnotatedMethod() :
        execution(@io.cucumber.java.en.When * *(..));

    pointcut azureB2CAuthenticationAnnotatedMethod() :
        execution(@org.lenze.nupano.suite.annotations.AzureB2CAuthentication * *(..));

    before() : whenAnnotatedMethod() {
        JoinPoint.StaticPart staticPart = thisJoinPointStaticPart;
        Object[] args = thisJoinPoint.getArgs();

        try {
            Method method = getMethodFromJoinPoint(thisJoinPoint);
            if (method != null) {
                When when = method.getAnnotation(When.class);
                if (when.value().contains("authenticated through")) {
                    int argCount = thisJoinPoint.getArgs().length;

                    String user = (String) args[argCount-2];
                    String organization = (String) args[argCount-1];
                    Serenity.environmentVariables().setProperty("nupanosuite_user", user + "@" + organization + ".com");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Object around() : azureB2CAuthenticationAnnotatedMethod() {
        try {
            Method method = getMethodFromJoinPoint(thisJoinPoint);
            if (method != null) {
                AzureB2CAuthentication annotation = method.getAnnotation(AzureB2CAuthentication.class);
                if (annotation.type().equals(AzureB2CAuthenticationType.PKCE)) {
                    new Azure().B2C();
                } else if (annotation.type().equals(AzureB2CAuthenticationType.UI)) {
                    new Authentication().AzureB2CAuthentication(SuiteProperties.activeStage.theActorInTheSpotlight());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return proceed(); // continue with original method
    }

    after() : azureB2CAuthenticationAnnotatedMethod() {
        try {
            Method method = getMethodFromJoinPoint(thisJoinPoint);
            if (method != null) {
                AzureB2CAuthentication annotation = method.getAnnotation(AzureB2CAuthentication.class);
                if (annotation.type().equals(AzureB2CAuthenticationType.PKCE)) {
                    SuiteProperties.activeStage.drawTheCurtain();
                    Serenity.getDriver().quit();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Method getMethodFromJoinPoint(JoinPoint joinPoint) {
        try {
            String methodName = joinPoint.getSignature().getName();
            Class<?>[] parameterTypes = ((org.aspectj.lang.reflect.MethodSignature) joinPoint.getSignature()).getMethod().getParameterTypes();
            Method method = joinPoint.getTarget().getClass().getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}