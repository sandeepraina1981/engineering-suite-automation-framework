package org.lenze.nupano.suite.authentiction.oauth2.authorizationcode.pkce;

import com.microsoft.aad.msal4j.*;
import net.serenitybdd.core.Serenity;
import org.lenze.nupano.suite.annotations.StageMember;
import org.lenze.nupano.suite.helper.SuiteProperties;
import org.lenze.nupano.suite.stepdefinitions.ui.Authentication;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Azure {
    @StageMember
    public void B2C() {
        String clientId = Serenity.environmentVariables().getProperty("azureb2c_clientId");
        String tenant = Serenity.environmentVariables().getProperty("azureb2c_tenant");
        String tenant_root = Serenity.environmentVariables().getProperty("azureb2c_tenant_root");
        String policy = Serenity.environmentVariables().getProperty("azureb2c_policy");
        String redirectUri = Serenity.environmentVariables().getProperty("azureb2c_redirectUri");
        String scope = Serenity.environmentVariables().getProperty("azureb2c_scope");

        String authority = String.format(
                "https://%s/%s/%s",
                tenant_root,
                tenant,
                policy
        );

        Set<String> scopes = Set.of(
                "openid",
                "profile",
                clientId
        );

//        Set<String> scopes = Arrays.stream(scope.split("\\s+")).collect(Collectors.toSet());

        PublicClientApplication pca = null;
        try {
            System.out.println("AUTHORITY=" + authority);

            IHttpClient crossCloudDiscoveryProxy = new IHttpClient() {
                private final java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();

                @Override
                public com.microsoft.aad.msal4j.IHttpResponse send(com.microsoft.aad.msal4j.HttpRequest msalRequest) throws Exception {
                    String requestUrl = msalRequest.url().toString();

                    // 1. Intercept instance discovery checks to bypass AADSTS50049 completely
                    if (requestUrl.contains("discovery/instance")) {
                        System.out.println("[PROXY] Intercepted instance discovery check. Injecting custom tenant trust data.");
                        String mockDiscoveryJson = String.format(
                                "{\"tenant_discovery_endpoint\":\"https://%s/%s/%s/v2.0/.well-known/openid-configuration\"," +
                                        "\"metadata\":[{\"preferred_network\":\"%s\",\"preferred_cache\":\"%s\",\"aliases\":[\"%s\"]}]}",
                                tenant_root, tenant, policy, tenant_root, tenant_root, tenant_root
                        );
                        return new com.microsoft.aad.msal4j.IHttpResponse() {
                            @Override public int statusCode() { return 200; }
                            @Override public String body() { return mockDiscoveryJson; }
                            @Override public java.util.Map<String, java.util.List<String>> headers() {
                                return java.util.Map.of("Content-Type", java.util.List.of("application/json"));
                            }
                        };
                    }

                    // 2. BULLETPROOF REWRITE: Catch any token call to your domain that is missing the policy path
                    if (requestUrl.contains(tenant_root) && !requestUrl.contains(policy)) {
                        String realTokenEndpoint = String.format("https://%s/%s/%s/oauth2/v2.0/token", tenant_root, tenant, policy);
                        System.out.println("[PROXY-REWRITE] Caught misrouted token call. Rerouting to: " + realTokenEndpoint);
                        requestUrl = realTokenEndpoint;
                    }

                    // 3. Rebuild and dispatch the outbound HTTP call safely
                    java.net.http.HttpRequest.Builder httpRequestBuilder = java.net.http.HttpRequest.newBuilder()
                            .uri(java.net.URI.create(requestUrl));

                    String bodyContent = msalRequest.body() != null ? msalRequest.body() : "";

                    if (msalRequest.httpMethod().name().equalsIgnoreCase("POST")) {
                        httpRequestBuilder.POST(java.net.http.HttpRequest.BodyPublishers.ofString(bodyContent));
                    } else {
                        httpRequestBuilder.GET();
                    }

                    // Copy all authorization and token exchange headers
                    if (msalRequest.headers() != null) {
                        msalRequest.headers().forEach(httpRequestBuilder::header);
                    }

                    java.net.http.HttpResponse<String> response = client.send(
                            httpRequestBuilder.build(),
                            java.net.http.HttpResponse.BodyHandlers.ofString()
                    );

                    return new com.microsoft.aad.msal4j.IHttpResponse() {
                        @Override public int statusCode() { return response.statusCode(); }
                        @Override public String body() { return response.body(); }
                        @Override public java.util.Map<String, java.util.List<String>> headers() { return response.headers().map(); }
                    };
                }
            };

            pca = PublicClientApplication.builder(clientId)
                    .authority(authority)
                    .validateAuthority(false)
                    .httpClient(crossCloudDiscoveryProxy ) // Overrides network actions completely
                    .build();
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
        }

        OpenBrowserAction browserAction = url -> {
            try {
                String loginUrl = url.toString();
                if (!loginUrl.contains("p=")) {
                    loginUrl += (loginUrl.contains("?") ? "&" : "?") + "p=" + policy;
                }

                System.out.println("LOGIN URL = " + loginUrl);

                Serenity.environmentVariables().setProperty("nupanosuite_url", loginUrl);
                new Authentication().AzureB2CAuthentication(SuiteProperties.activeStage.theActorInTheSpotlight());

            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        InteractiveRequestParameters parameters = null;
        try {
            parameters = InteractiveRequestParameters.builder(new URI(redirectUri))
                    .scopes(scopes)
//                    .prompt(Prompt.LOGIN)
                    .systemBrowserOptions(SystemBrowserOptions.builder().openBrowserAction(browserAction).build())
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        System.out.println("param: " + parameters);
        CompletableFuture<IAuthenticationResult> future = pca.acquireToken(parameters);
        IAuthenticationResult result = null;
        try {
            System.out.println("Waiting for token...");
            result = future.get();
            System.out.println("Result = " + result);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            System.out.println("CAUSE=");
            throw new RuntimeException(
                    "Azure B2C authentication failed",
                    e.getCause()
            );
        }

        if (result == null) {
            throw new RuntimeException(
                    "Azure B2C authentication failed. No token received.");
        }

        Serenity.environmentVariables().setProperty("azureb2c_accesstoken", result.accessToken());
        Serenity.environmentVariables().setProperty("azureb2c_tokenID", result.idToken());
    }
}
