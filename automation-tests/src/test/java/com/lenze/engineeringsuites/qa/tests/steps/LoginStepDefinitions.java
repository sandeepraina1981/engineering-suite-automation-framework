package com.lenze.engineeringsuites.qa.tests.steps;

import com.lenze.engineeringsuites.qa.framework.actor.EngineeringSuiteCast;
import com.lenze.engineeringsuites.qa.framework.actor.Persona;
import com.lenze.engineeringsuites.qa.tests.tasks.LoginToEngineeringSuite;
import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import net.serenity_bdd.screenplay.Actor;
import net.serenity_bdd.screenplay.actors.OnStage;

public class LoginStepDefinitions {

    @Before
    public void setTheStage() {
        // Now using custom ability-driven casting
        OnStage.setTheStage(new EngineeringSuiteCast());
    }

    @ParameterType(".*")
    public Persona persona(String name) {
        for (Persona persona : Persona.values()) {
            if (persona.getRealName().equalsIgnoreCase(name)) {
                return persona;
            }
        }
        throw new IllegalArgumentException("Persona not found: " + name);
    }

    @Given("{persona} is on the login page")
    public void actorIsOnTheLoginPage(Persona persona) {
        Actor actor = OnStage.theActorCalled(persona.getRealName());
        // Custom setup logic for the Persona's browser could go here
    }

    @When("{persona} logs in with valid credentials")
    public void actorLogsIn(Persona persona) {
        Actor actor = OnStage.theActorCalled(persona.getRealName());
        // Credentials can be fetched from a secure store based on UserLevel
        actor.attemptsTo(
                LoginToEngineeringSuite.withCredentials("test@lenze.com", "Password123")
        );
    }

    @Then("{persona} should see the dashboard")
    public void actorShouldSeeDashboard(Persona persona) {
        Actor actor = OnStage.theActorCalled(persona.getRealName());
        // Assertion logic
    }
}
