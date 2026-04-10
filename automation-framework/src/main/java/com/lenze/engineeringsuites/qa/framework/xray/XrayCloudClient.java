package com.lenze.engineeringsuites.qa.framework.xray;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class XrayCloudClient {

    private static final String XRAY_AUTH_URL = "https://xray.cloud.getxray.app/api/v2/authenticate";
    private static final String XRAY_IMPORT_URL = "https://xray.cloud.getxray.app/api/v2/import/execution/cucumber";
    
    private String clientId;
    private String clientSecret;
    private String authToken;
    private Playwright playwright;
    private APIRequestContext requestContext;

    public XrayCloudClient(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.playwright = Playwright.create();
        this.requestContext = playwright.request().newContext();
        this.authToken = authenticate();
    }

    private String authenticate() {
        Map<String, String> body = new HashMap<>();
        body.put("client_id", clientId);
        body.put("client_secret", clientSecret);

        APIResponse response = requestContext.post(XRAY_AUTH_URL, RequestOptions.create().setData(body));
        
        if (response.status() == 200) {
            return response.text().replace("\"", "");
        } else {
            throw new RuntimeException("Xray authentication failed: " + response.statusText());
        }
    }

    public void importCucumberResults(File jsonResultFile) {
        try {
            byte[] fileContent = Files.readAllBytes(jsonResultFile.toPath());
            APIResponse response = requestContext.post(XRAY_IMPORT_URL, RequestOptions.create()
                    .setHeader("Authorization", "Bearer " + authToken)
                    .setHeader("Content-Type", "application/json")
                    .setData(fileContent));

            if (response.status() != 200) {
                System.err.println("Failed to import results to Xray: " + response.statusText());
                System.err.println(response.text());
            } else {
                System.out.println("Test results imported to Xray successfully.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading Cucumber JSON file", e);
        }
    }

    public void close() {
        if (playwright != null) {
            playwright.close();
        }
    }
}
