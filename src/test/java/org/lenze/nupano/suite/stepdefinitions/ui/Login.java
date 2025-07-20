package org.lenze.nupano.suite.stepdefinitions.ui;

import net.serenitybdd.core.Serenity;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.Open;
import net.serenitybdd.screenplay.waits.WaitUntil;

import java.time.Duration;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class Login {
    public static Performable viaStoredAuth() {
        return Task.where(
                Open.url(Serenity.environmentVariables().getProperty("nupanosuite_url")),
                WaitUntil.the("#signInName", isVisible()).forNoMoreThan(Duration.ofMinutes(3)),
                Enter.theValue(Serenity.environmentVariables().getProperty("nupanosuite_user")).into("#signInName"),
                Click.on("#continue"),
                WaitUntil.the("#password", isVisible()).forNoMoreThan(Duration.ofMinutes(3)),
                Enter.theValue(Serenity.environmentVariables().getProperty("nupanosuite_password")).into("#password"),
                Click.on("#next")
        );
    }
}
