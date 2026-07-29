# 📘 Azure AD B2C Authentication Framework using MSAL4J, Serenity BDD and PKCE

## 📖 Overview

This document describes the implementation of an OAuth 2.0 Authorization Code Flow with PKCE using:

- Azure AD B2C
- MSAL4J (Microsoft Authentication Library for Java)
- Serenity BDD
- Selenium WebDriver
- Microsoft Edge Browser

The solution is designed to automate Azure AD B2C authentication during test execution and securely acquire OAuth tokens that can be reused across UI and API test scenarios.

Unlike a standard MSAL4J implementation, this solution includes:

- Dynamic configuration through Serenity properties
- Azure AD B2C policy-aware authentication
- Custom HTTP client implementation
- Azure AD B2C instance discovery bypass
- Automatic token endpoint correction
- Automatic token storage within Serenity runtime context

---

# 🧪 Integration with Serenity BDD

## 🔗 Purpose

The authentication mechanism is tightly integrated with the Serenity BDD framework to support automated authentication for:

- UI Testing
- API Testing
- End-to-End Testing
- BDD Scenarios

Authentication is performed once and the resulting tokens are made available to all subsequent test steps.

---

## ✅ Benefits

| Benefit | Description |
|----------|-------------|
| Automated Authentication | Eliminates manual login processes. |
| Token Reusability | Access and ID Tokens are stored and reused throughout test execution. |
| Centralized Configuration | Azure B2C settings managed through Serenity environment variables. |
| End-to-End Coverage | Supports UI login followed by API validation. |
| Security Compliance | Uses OAuth 2.0 Authorization Code Flow with PKCE. |
| Azure B2C Compatibility | Supports Azure AD B2C custom policies. |
| Scalable Design | Supports multiple environments and tenants. |

---

# 🧩 Architecture Components

| Component | Description |
|------------|------------|
| Azure AD B2C | Identity Provider |
| MSAL4J | OAuth/OIDC Authentication Library |
| Serenity BDD | Test Automation Framework |
| Selenium WebDriver | Browser Automation |
| Edge Browser | Authentication UI Execution |
| Custom IHttpClient | Handles discovery bypass and endpoint rewrites |
| PKCE Flow | Secure Authorization Code Exchange |

---

# ⚙️ Configuration

All Azure AD B2C settings are obtained dynamically from Serenity environment variables.

## Environment Properties

```properties
azureb2c_clientId=
azureb2c_tenant=
azureb2c_tenant_root=
azureb2c_policy=
azureb2c_redirectUri=
azureb2c_scope=
```

## Example Configuration

```properties
azureb2c_clientId=20fea690-0a64-4a1e-9502-7b52c55f2bab
azureb2c_tenant=lenzeb2cdev.onmicrosoft.com
azureb2c_tenant_root=lenzeb2cdev.b2clogin.com
azureb2c_policy=b2c_1a_signup_signin
azureb2c_redirectUri=http://localhost:3256
azureb2c_scope=openid profile
```

---

# 🔄 Authentication Flow

```text
Serenity Test
      │
      ▼
Azure.B2C()
      │
      ▼
Load Azure Configuration
      │
      ▼
Create Custom HTTP Client
      │
      ▼
Build MSAL Public Client
      │
      ▼
Launch Browser Authentication
      │
      ▼
Authentication.AzureB2CAuthentication()
      │
      ▼
User Login
      │
      ▼
Authorization Code Received
      │
      ▼
MSAL Exchanges Code for Tokens
      │
      ▼
Access Token + ID Token
      │
      ▼
Store Tokens in Serenity Context
```

---

# 🧪 Code Walkthrough

## 1. Load Configuration

Azure AD B2C configuration values are loaded from Serenity environment variables.

```java
String clientId =
    Serenity.environmentVariables()
            .getProperty("azureb2c_clientId");

String tenant =
    Serenity.environmentVariables()
            .getProperty("azureb2c_tenant");
```

### Purpose

- Environment-independent execution
- Secure configuration management
- Multi-environment support

---

## 2. Build Authority URL

Authority URL is generated dynamically.

```java
String authority = String.format(
    "https://%s/%s/%s",
    tenant_root,
    tenant,
    policy
);
```

### Example

```text
https://lenzeb2cdev.b2clogin.com/
lenzeb2cdev.onmicrosoft.com/
b2c_1a_signup_signin
```

---

## 3. Configure OAuth Scopes

Scopes include:

```java
Set<String> scopes = Set.of(
    "openid",
    "profile",
    clientId
);
```

### Purpose

- Obtain ID token
- Retrieve user profile claims
- Acquire access token

---

# 🔧 Custom HTTP Client

## Purpose

A custom MSAL4J `IHttpClient` implementation is used to intercept and manipulate network communication with Azure AD B2C.

```java
IHttpClient crossCloudDiscoveryProxy
```

This solves Azure AD B2C authority validation and endpoint routing issues.

---

# 🚫 Azure AD Instance Discovery Bypass

## Problem

MSAL performs authority validation through Azure instance discovery.

In Azure AD B2C custom domains or custom policy configurations this can trigger errors such as:

```text
AADSTS50049:
Unknown or invalid instance
```

---

## Solution

The framework intercepts all discovery requests:

```java
if (requestUrl.contains("discovery/instance"))
```

and returns a custom discovery response.

```json
{
  "tenant_discovery_endpoint": "https://tenant/policy/v2.0/.well-known/openid-configuration",
  "metadata": [
    {
      "preferred_network": "tenant",
      "preferred_cache": "tenant",
      "aliases": ["tenant"]
    }
  ]
}
```

### Benefits

- Prevents AADSTS50049 errors
- Supports custom B2C authorities
- Reduces dependency on Azure instance discovery

---

# 🔄 Token Endpoint Rewrite Mechanism

## Problem

MSAL may occasionally submit token requests without the Azure AD B2C policy path.

Incorrect endpoint:

```text
https://tenant.b2clogin.com/oauth2/v2.0/token
```

Required endpoint:

```text
https://tenant.b2clogin.com/tenant/policy/oauth2/v2.0/token
```

---

## Solution

Every outgoing request is inspected.

```java
if (requestUrl.contains(tenant_root)
    && !requestUrl.contains(policy))
```

The URL is rewritten automatically.

```java
requestUrl = realTokenEndpoint;
```

### Benefits

- Guarantees correct policy routing
- Eliminates token acquisition failures
- Supports Azure AD B2C custom policies

---

# 🌐 Browser Authentication Integration

Instead of launching a Selenium browser directly inside the authentication class, the framework delegates login execution to Serenity.

```java
OpenBrowserAction browserAction = url -> {
    ...
    new Authentication()
        .AzureB2CAuthentication(
            SuiteProperties.activeStage.theActorInTheSpotlight()
        );
};
```

---

## Policy Injection

The framework verifies that the Azure AD B2C policy parameter exists.

```java
if (!loginUrl.contains("p="))
```

If missing, it is appended automatically.

```java
loginUrl += "&p=" + policy;
```

### Benefits

- Consistent policy selection
- Simplified authentication URLs
- Reduced configuration errors

---

# 🔐 MSAL Public Client Configuration

The MSAL Public Client Application is configured with:

- Dynamic Authority URL
- Disabled Authority Validation
- Custom HTTP Client

```java
pca = PublicClientApplication.builder(clientId)
        .authority(authority)
        .validateAuthority(false)
        .httpClient(crossCloudDiscoveryProxy)
        .build();
```

### Key Features

| Feature | Purpose |
|----------|---------|
| authority() | Azure AD B2C authority endpoint |
| validateAuthority(false) | Disables Azure instance validation |
| httpClient() | Custom HTTP interception and rewriting |

---

# 🔐 Token Acquisition

Authentication is initiated using MSAL4J Interactive Authentication.

```java
InteractiveRequestParameters parameters =
    InteractiveRequestParameters.builder(
        new URI(redirectUri))
        .scopes(scopes)
        .systemBrowserOptions(
            SystemBrowserOptions.builder()
                .openBrowserAction(browserAction)
                .build()
        )
        .build();
```

---

## Acquire Token

```java
CompletableFuture<IAuthenticationResult> future =
        pca.acquireToken(parameters);

IAuthenticationResult result =
        future.get();
```

---

# 🎫 Token Storage

After successful authentication, the acquired tokens are stored inside Serenity's runtime environment.

```java
Serenity.environmentVariables()
    .setProperty(
        "azureb2c_accesstoken",
        result.accessToken()
    );

Serenity.environmentVariables()
    .setProperty(
        "azureb2c_tokenID",
        result.idToken()
    );
```

---

## Stored Runtime Variables

| Variable | Description |
|-----------|------------|
| azureb2c_accesstoken | OAuth Access Token |
| azureb2c_tokenID | OpenID Connect ID Token |

These values can later be consumed by:

- REST API Tests
- Service Layer Tests
- Authorization Validation Tests
- Backend Integration Tests

Example:

```java
String token =
    Serenity.environmentVariables()
            .getProperty("azureb2c_accesstoken");
```

---

# ❗ Error Handling

## Authentication Failure

If no token is returned:

```java
if (result == null) {
    throw new RuntimeException(
        "Azure B2C authentication failed. No token received."
    );
}
```

---

## Token Acquisition Failure

Execution exceptions are captured and wrapped with a meaningful message.

```java
catch (ExecutionException e) {
    throw new RuntimeException(
        "Azure B2C authentication failed",
        e.getCause()
    );
}
```

---

# 🔐 Security Considerations

## Recommended Practices

✅ Store Azure configuration in Serenity property files

✅ Use secure credential vaults

✅ Use dedicated test accounts

✅ Use PKCE-based authentication flow

✅ Protect access tokens from log exposure

✅ Restrict token access to authorized test components

---

## Avoid

❌ Hardcoding usernames and passwords

❌ Storing secrets in source control

❌ Logging access tokens in CI/CD pipelines

❌ Sharing credentials between environments

❌ Exposing Azure AD B2C configuration publicly

---

# 🛠️ Prerequisites

- Java 11+
- Serenity BDD
- MSAL4J
- Selenium WebDriver
- Microsoft Edge Browser
- EdgeDriver
- Azure AD B2C Tenant
- Azure AD B2C App Registration
- Configured Azure AD B2C User Flow or Custom Policy

---

# 🚀 Key Enhancements Over Previous Implementation

| Enhancement | Status |
|------------|---------|
| Serenity Environment Configuration | ✅ |
| Dynamic Authority Construction | ✅ |
| Automatic Policy Injection | ✅ |
| Custom HTTP Client | ✅ |
| Azure AD Instance Discovery Bypass | ✅ |
| Token Endpoint Rewrite | ✅ |
| Authority Validation Bypass | ✅ |
| Runtime Token Storage | ✅ |
| Serenity Actor Integration | ✅ |
| Azure AD B2C Custom Policy Support | ✅ |

---

# 📈 End-to-End Sequence Diagram

```text
Serenity Test
      │
      ▼
Azure.B2C()
      │
      ▼
Read Environment Variables
      │
      ▼
Create Custom IHttpClient
      │
      ▼
Build PublicClientApplication
      │
      ▼
Launch OpenBrowserAction
      │
      ▼
Authentication.AzureB2CAuthentication()
      │
      ▼
Azure AD B2C Login
      │
      ▼
Authorization Code Returned
      │
      ▼
MSAL4J Token Exchange
      │
      ▼
Access Token + ID Token Acquired
      │
      ▼
Store Tokens in Serenity Environment
      │
      ▼
Consume Tokens in API/UI Tests
```

---

# ✅ Conclusion

This implementation provides a robust, Serenity-integrated Azure AD B2C authentication mechanism that supports OAuth 2.0 Authorization Code Flow with PKCE while overcoming common Azure AD B2C authority validation and token routing challenges.

Key capabilities include:

- Azure AD B2C Custom Policy Support
- Automated Browser-Based Authentication
- Custom MSAL Network Handling
- Discovery Endpoint Interception
- Token Endpoint Rewriting
- Runtime Token Sharing Across Serenity Tests
- Secure Token Acquisition and Storage

The solution enables seamless authentication for UI, API, and end-to-end automation scenarios within the Serenity BDD ecosystem.