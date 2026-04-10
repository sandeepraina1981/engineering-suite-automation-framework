package com.lenze.engineeringsuites.qa.tests.interactions;

import net.serenity_bdd.screenplay.Actor;
import net.serenity_bdd.screenplay.Interaction;
import net.serenity_bdd.screenplay.playwright.abilities.BrowseTheWebWithPlaywright;
import static net.serenity_bdd.screenplay.Tasks.instrumented;

/**
 * A custom Interaction to handle specific Playwright actions that aren't
 * covered by standard Serenity interactions, or to provide a domain-specific action.
 */
public class SelectFromCustomDropdown implements Interaction {

    private final String dropdownSelector;
    private final String optionText;

    public SelectFromCustomDropdown(String dropdownSelector, String optionText) {
        this.dropdownSelector = dropdownSelector;
        this.optionText = optionText;
    }

    public static SelectFromCustomDropdown called(String dropdownSelector, String optionText) {
        return instrumented(SelectFromCustomDropdown.class, dropdownSelector, optionText);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        // Accessing the Playwright Page object via the BrowseTheWebWithPlaywright ability
        var page = BrowseTheWebWithPlaywright.as(actor).getInternalPage();
        
        page.click(dropdownSelector);
        page.click(String.format("text=%s", optionText));
    }
}
