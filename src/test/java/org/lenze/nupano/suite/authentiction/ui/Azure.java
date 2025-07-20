package org.lenze.nupano.suite.authentiction.ui;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.core.Serenity;
import org.lenze.nupano.suite.enummeration.AzureB2CAuthenticationType;
import org.lenze.nupano.suite.helper.SuiteStage;
import org.lenze.nupano.suite.helper.SuiteProperties;
import org.lenze.nupano.suite.interactions.AzureB2CLogin;
import static org.assertj.core.api.Assertions.assertThat;

public class Azure extends SuiteStage {
    @Step("Authenticate with credentials provided")
    public void SignIn() {
        Serenity.reportThat("Opening browser to: " + Serenity.environmentVariables().getProperty("nupanosuite_url"),
                () -> assertThat(true).isEqualTo(true));

        if (Serenity.environmentVariables().getProperty("azureB2CAuthenticationType").equalsIgnoreCase(AzureB2CAuthenticationType.PKCE.getType())) {
            SuiteProperties.activeStage = stageUI();
            SuiteProperties.activeStage.shineSpotlightOn("Derek");
        }

        SuiteProperties.activeStage.theActorInTheSpotlight().attemptsTo(AzureB2CLogin.viaStoredAuth());
    }
}