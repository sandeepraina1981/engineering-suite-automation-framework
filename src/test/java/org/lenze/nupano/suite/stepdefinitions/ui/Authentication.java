package org.lenze.nupano.suite.stepdefinitions.ui;

import io.cucumber.java.en.Given;
import net.serenitybdd.screenplay.Actor;
import org.lenze.nupano.suite.helper.SuiteProperties;
import org.lenze.nupano.suite.screenplay.actions.AzureB2CLogin;

public class Authentication {
    @Given("{actorOrgAPI} Performs Azure B2C Authentication with credentials provided")
    public void AzureB2CAuthentication(Actor actor) {
        actor.attemptsTo(AzureB2CLogin.viaStoredAuth());
    }
}
