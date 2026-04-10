package com.lenze.engineeringsuites.qa.framework.actor;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Page;
import net.serenity_bdd.screenplay.Actor;
import net.serenity_bdd.screenplay.playwright.abilities.BrowseTheWebWithPlaywright;

/**
 * BaseActor provides a standardized way to create and configure Actors 
 * with the necessary abilities based on their Persona and Role.
 */
public class BaseActor {

    /**
     * Factory method to create an actor based on a Persona.
     */
    public static Actor withPersona(Persona persona, Object ability) {
        Actor actor = Actor.named(persona.getRealName());
        
        // Map abilities strictly based on persona intent
        if (ability instanceof Page && persona.getPreferredBrowser() != null) {
            actor.can(BrowseTheWebWithPlaywright.using((Page) ability));
        } else if (ability instanceof APIRequestContext) {
            actor.can(CallAnApiWithPlaywright.using((APIRequestContext) ability));
        }
        
        // Store persona data in actor's memory for decision logic in tasks
        actor.remember("USER_LEVEL", persona.getUserLevel());
        actor.remember("PERSONA", persona);
        
        return actor;
    }
}
