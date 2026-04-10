package com.lenze.engineeringsuites.qa.tests.questions;

import net.serenity_bdd.screenplay.Actor;
import net.serenity_bdd.screenplay.Question;
import net.serenity_bdd.screenplay.playwright.abilities.BrowseTheWebWithPlaywright;

/**
 * A custom Question to retrieve data from the application state for assertions.
 */
public class CurrentDashboardTitle implements Question<String> {

    public static CurrentDashboardTitle displayed() {
        return new CurrentDashboardTitle();
    }

    @Override
    public String answeredBy(Actor actor) {
        // Accessing the Playwright Page object via the BrowseTheWebWithPlaywright ability
        var page = BrowseTheWebWithPlaywright.as(actor).getInternalPage();
        
        // Return the text content of the dashboard header element
        return page.textContent("h1.dashboard-title").trim();
    }
}
