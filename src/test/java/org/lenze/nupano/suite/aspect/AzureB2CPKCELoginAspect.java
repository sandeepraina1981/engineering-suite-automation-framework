package org.lenze.nupano.suite.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.lenze.nupano.suite.annotations.AzureB2CPKCELogin;
import org.lenze.nupano.suite.authentiction.oauth2.authorizationcode.pkce.Azure;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AzureB2CPKCELoginAspect {
    @Before("@annotation(azureB2CPKCELogin)")
    public void executeAnnotatedMethod(JoinPoint joinPoint, AzureB2CPKCELogin azureB2CPKCELogin) throws Throwable {
        System.out.println("Executing before the annotated method: " + joinPoint.getSignature().getName());
        System.out.println("Annotation value: " + azureB2CPKCELogin.value());

        new Azure().B2C();

        System.out.println("Executing after the annotated method: " + joinPoint.getSignature().getName());
    }
}
