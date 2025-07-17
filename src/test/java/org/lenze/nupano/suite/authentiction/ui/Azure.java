package org.lenze.nupano.suite.authentiction.ui;

import net.serenitybdd.core.Reportable;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.Open;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.junit.jupiter.api.DisplayName;
import org.lenze.nupano.suite.helpers.SuiteStage;
import org.lenze.nupano.suite.properties.SuiteProperties;
import org.openqa.selenium.By;
import java.time.Duration;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class Azure extends SuiteStage {
    @DisplayName("Authenticate with credentials provided")
    public boolean SignIn() {
        System.out.println("Opening browser to: " + Serenity.environmentVariables().getProperty("nupanosuite_url"));

        Actor actorAuth;
        boolean closeWebDriver = false;

        if (Serenity.getWebdriverManager().getActiveWebdriverCount() == 0) {
            closeWebDriver = true;
            SuiteProperties.activeStage = stageUI();
            actorAuth = SuiteProperties.activeStage.shineSpotlightOn("Drew UI");

            System.out.println("Web driver: " + Serenity.getWebdriverManager().getActiveWebdriverCount());
        }
        else {
            actorAuth = SuiteProperties.activeStage.theActorInTheSpotlight();
        }

        actorAuth.attemptsTo(Open.url(Serenity.environmentVariables().getProperty("nupanosuite_url")));
        actorAuth.attemptsTo(
                WaitUntil.the(By.id("signInName"), isVisible()).forNoMoreThan(Duration.ofMinutes(3)),
                Enter.theValue(Serenity.environmentVariables().getProperty("nupanosuite_user")).into(By.id("signInName")),
                Click.on(By.id("continue")));

        actorAuth.attemptsTo(
                WaitUntil.the(By.id("password"), isVisible()).forNoMoreThan(Duration.ofMinutes(3)),
                Enter.theValue(Serenity.environmentVariables().getProperty("nupanosuite_password")).into(By.id("password")),
                Click.on(By.cssSelector("button[type='submit']")));

        return closeWebDriver;
    }
}
