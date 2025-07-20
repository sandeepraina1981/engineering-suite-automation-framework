package org.lenze.nupano.suite.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME) // Available at runtime
@Target(ElementType.METHOD)
@Documented
public @interface AzureB2CAuthentication {
    String type() default "PKCE";
}
