package org.lenze.nupano.suite.interactions;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.steps.UIInteractions;
import net.serenitybdd.screenplay.*;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.Open;
import org.lenze.nupano.suite.uitargets.LoginTargets;

public class AzureB2CLogin extends UIInteractions implements LoginTargets, Task {
    @Override
    @Step("{0} authenticates Azure B2C login page")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Open.url(Serenity.environmentVariables().getProperty("nupanosuite_url")),
            Enter.theValue(Serenity.environmentVariables().getProperty("nupanosuite_user")).into(txt_username.resolveFor(actor)),
            Click.on(bttn_continue.resolveFor(actor)),
            Enter.theValue(Serenity.environmentVariables().getProperty("nupanosuite_password")).into(txt_password.resolveFor(actor)),
            Click.on(bttn_next.resolveFor(actor))
        );
    }

    public static AzureB2CLogin viaStoredAuth() {
        return Tasks.instrumented(AzureB2CLogin.class);
    }
}