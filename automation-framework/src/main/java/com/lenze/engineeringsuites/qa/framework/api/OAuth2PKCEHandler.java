package com.lenze.engineeringsuites.qa.framework.api;

import com.microsoft.playwright.Page;
import lombok.Getter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class OAuth2PKCEHandler {

    @Getter
    private String codeVerifier;
    @Getter
    private String codeChallenge;

    public OAuth2PKCEHandler() {
        this.codeVerifier = generateCodeVerifier();
        this.codeChallenge = generateCodeChallenge(this.codeVerifier);
    }

    private String generateCodeVerifier() {
        SecureRandom sr = new SecureRandom();
        byte[] code = new byte[32];
        sr.nextBytes(code);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(code);
    }

    private String generateCodeChallenge(String codeVerifier) {
        try {
            byte[] bytes = codeVerifier.getBytes(StandardCharsets.US_ASCII);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(bytes, 0, bytes.length);
            byte[] digest = md.digest();
            return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating code challenge", e);
        }
    }

    /**
     * Navigates the B2C login screen using Playwright to handle the interactive flow.
     */
    public String loginAndGetCode(Page page, String authUrl, String username, String password) {
        page.navigate(authUrl);
        page.fill("input[type='email']", username);
        page.fill("input[type='password']", password);
        page.click("button#next"); // Selector based on B2C template

        // Wait for redirect and extract code from URL
        page.waitForURL(url -> url.contains("code="));
        String url = page.url();
        return url.split("code=")[1].split("&")[0];
    }
}
