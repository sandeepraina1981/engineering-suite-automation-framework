package com.lenze.engineeringsuites.qa.tests.tasks;

import com.lenze.engineeringsuites.qa.framework.api.CredentialsProvider;
import com.lenze.engineeringsuites.qa.framework.actor.UserLevel;
import net.serenity_bdd.screenplay.Actor;
import net.serenity_bdd.screenplay.Task;
import net.serenity_bdd.screenplay.actions.Click;
import net.serenity_bdd.screenplay.actions.Enter;
import net.serenity_bdd.screenplay.playwright.interactions.Open;
import static net.serenity_bdd.screenplay.Tasks.instrumented;

/**
 * Intelligent Login Task that pulls credentials securely based on the Actor's Role.
 */
public class LoginToEngineeringSuite implements Task {

    public static LoginToEngineeringSuite automatically() {
        return instrumented(LoginToEngineeringSuite.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        UserLevel userLevel = actor.recall("USER_LEVEL");
        
        String username = CredentialsProvider.getUsernameFor(userLevel);
        String password = CredentialsProvider.getPasswordFor(userLevel);

        actor.attemptsTo(
                Open.browserOn("https://qa.engineeringsuites.lenze.com"),
                Enter.theValue(username).into("input[type='email']"),
                Enter.theValue(password).into("input[type='password']"),
                Click.on("button#next")
        );
    }
}
