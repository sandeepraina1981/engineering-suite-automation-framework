package com.lenze.engineeringsuites.qa.framework.actor;

import com.microsoft.playwright.APIRequestContext;
import net.serenity_bdd.screenplay.Ability;
import net.serenity_bdd.screenplay.Actor;

/**
 * Custom Serenity Screenplay Ability to use Playwright's APIRequestContext
 * for performing REST API calls.
 */
public class CallAnApiWithPlaywright implements Ability {

    private final APIRequestContext requestContext;

    private CallAnApiWithPlaywright(APIRequestContext requestContext) {
        this.requestContext = requestContext;
    }

    public static CallAnApiWithPlaywright using(APIRequestContext requestContext) {
        return new CallAnApiWithPlaywright(requestContext);
    }

    public static CallAnApiWithPlaywright as(Actor actor) {
        if (actor.abilityTo(CallAnApiWithPlaywright.class) == null) {
            throw new RuntimeException("The actor " + actor.getName() + " does not have the ability to call an API with Playwright.");
        }
        return actor.abilityTo(CallAnApiWithPlaywright.class);
    }

    public APIRequestContext getRequestContext() {
        return requestContext;
    }

    @Override
    public String toString() {
        return "Call an API with Playwright";
    }
}
