package com.lenze.engineeringsuites.qa.framework.actor;

import net.serenity_bdd.screenplay.Ability;
import net.serenity_bdd.screenplay.Actor;
import net.serenity_bdd.screenplay.actors.Cast;
import net.serenity_bdd.screenplay.playwright.abilities.BrowseTheWebWithPlaywright;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType.LaunchOptions;

/**
 * Enterprise Cast that dynamically creates environments and configures 
 * Actors with Roles and Abilities based on their Persona.
 */
public class EngineeringSuiteCast extends Cast {

    @Override
    public Actor actorNamed(String name, Ability... abilities) {
        Persona persona = Persona.fromRealName(name);
        Actor actor = super.actorNamed(name, abilities);

        // 1. Ability-Driven Environment Creation
        if (persona.getPreferredBrowser() != null) {
            // UI Environment Setup: Launch specific browser based on Persona
            System.out.println("DEBUG: Launching " + persona.getPreferredBrowser() + " for " + name);
            // In a real execution, we'd initialize the Playwright Page here 
            // and equip the actor with BrowseTheWebWithPlaywright
        } else {
            // API Environment Setup: Initialize Playwright APIRequestContext
            System.out.println("DEBUG: Initializing API Context for " + name);
            // Equip with CallAnApiWithPlaywright
        }

        // 2. Role-Based Data Association (Stored in Actor Memory)
        // This allows Login Tasks to automatically pull the correct credentials
        actor.remember("USER_LEVEL", persona.getUserLevel());
        
        return actor;
    }
}
