package com.lenze.engineeringsuites.qa.tests.tasks;

import com.lenze.engineeringsuites.qa.framework.actor.CallAnApiWithPlaywright;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import net.serenity_bdd.screenplay.Actor;
import net.serenity_bdd.screenplay.Task;
import static net.serenity_bdd.screenplay.Tasks.instrumented;

public class GetApiData implements Task {

    private final String endpoint;

    public GetApiData(String endpoint) {
        this.endpoint = endpoint;
    }

    public static GetApiData from(String endpoint) {
        return instrumented(GetApiData.class, endpoint);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        var requestContext = CallAnApiWithPlaywright.as(actor).getRequestContext();
        
        APIResponse response = requestContext.get(endpoint, RequestOptions.create());
        
        if (response.status() != 200) {
            throw new RuntimeException("API Call failed with status: " + response.status());
        }
        
        // Store response in actor's memory
        actor.remember("LAST_API_RESPONSE", response.text());
    }
}
