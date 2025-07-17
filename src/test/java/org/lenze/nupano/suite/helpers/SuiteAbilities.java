package org.lenze.nupano.suite.helpers;

import net.serenitybdd.core.Serenity;
import net.serenitybdd.screenplay.Ability;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actors.Cast;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import org.lenze.nupano.suite.properties.SuiteProperties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class SuiteAbilities extends SuiteActors {
    public Ability[] uiAbility() {
        Ability[] abilitySuite = {BrowseTheWeb.with(Serenity.getDriver())};
        return abilitySuite;
    }

    public Ability[] authAbility() {
        Serenity.getWebdriverManager().setCurrentDriver(Serenity.getDriver());
        Ability[] abilitySuite = {BrowseTheWeb.with(Serenity.getWebdriverManager().getCurrentDriver())};
        return abilitySuite;
    }

    public Ability[] apiAbility(String serviceContext) {
        Ability[] abilitySuite = {CallAnApi.at("https://" + serviceContext + ".api." + Serenity.environmentVariables().getProperty("suite_id") + ".dev.nupanosuite.lenze.com")};
        return abilitySuite;
    }
}
