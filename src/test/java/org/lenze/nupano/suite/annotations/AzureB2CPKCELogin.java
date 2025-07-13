package org.lenze.nupano.suite.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // Available at runtime
@Target(ElementType.METHOD)
public @interface AzureB2CPKCELogin {
    String value() default "";
}
