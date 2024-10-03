package org.lenze.nupano.suite.helpers;

import net.serenitybdd.core.Serenity;
import net.serenitybdd.screenplay.Ability;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actors.Cast;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;

public class SuiteAbilities extends SuiteActors {
    public Ability[] uiAbility() {
        Ability[] abilitySuite = {BrowseTheWeb.with(Serenity.getDriver())};
        return abilitySuite;
    }

    public Ability[] apiAbility() {
        Ability[] abilitySuite = {CallAnApi.at(Serenity.environmentVariables().getProperty("suite_url"))};
        return abilitySuite;
    }
}
