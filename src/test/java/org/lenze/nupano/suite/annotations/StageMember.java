package org.lenze.nupano.suite.annotations;

import org.lenze.nupano.suite.enummeration.AzureB2CAuthenticationType;
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StageMember {
    AzureB2CAuthenticationType type() default AzureB2CAuthenticationType.PKCE;
}
