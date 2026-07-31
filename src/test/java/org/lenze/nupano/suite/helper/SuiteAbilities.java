package org.lenze.nupano.suite.helper;

import net.serenitybdd.core.Serenity;
import net.serenitybdd.screenplay.Ability;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;

public class SuiteAbilities extends SuiteActors {
    public Ability[] uiAbility() {
        Ability[] abilitySuite = {BrowseTheWeb.with(Serenity.getDriver())};
        return abilitySuite;
    }

    public Ability[] apiAbility(String serviceContext) {
        Ability[] abilitySuite = {CallAnApi.at("https://" + System.getenv("SUITE_ID") + "." + serviceContext + ".api" + ".dev.engineering-suite.lenze.com")};
        return abilitySuite;
    }
}