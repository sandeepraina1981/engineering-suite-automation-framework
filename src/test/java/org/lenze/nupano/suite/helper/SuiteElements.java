package org.lenze.nupano.suite.helper;

import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.actions.Evaluate;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.openqa.selenium.By;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isPresent;

public class SuiteElements extends PageObject {
    private final String autoScrollElement = "arguments[0].scrollIntoView({ behavior: \"instant\", block: \"center\", inline: \"center\" });";
    private WebElementFacade currentPageElement;
    private By currentPageElementLocator;

    WebElementFacade findElement(String webElementName) {
        this.currentPageElementLocator = By.xpath(SuiteProperties.suitePageElements.get(webElementName));

        this.currentPageElement = find(this.currentPageElementLocator);
        WaitUntil.the(this.currentPageElementLocator, isPresent()).forNoMoreThan(Long.parseLong(Serenity.environmentVariables().getProperty("element_load_time"))).seconds();

        SuiteProperties.activeStage.theActorInTheSpotlight().attemptsTo(Evaluate.javascript(this.autoScrollElement, this.currentPageElement));
        return this.currentPageElement;
    }
}
