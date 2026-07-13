package demo.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.FilePayload;
import com.microsoft.playwright.options.FormData;
import com.microsoft.playwright.options.RequestOptions;
import demo.playwright.PwSession;
import helper.Json;
import pojo.LoginReq;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ApiClient {

    private APIRequest request;
    private APIRequestContext api;
    // PwSession session = new PwSession();
    ObjectMapper mapper = new ObjectMapper();
    public static String baseUrl;
    public static String tokenID;
    public static String userID;

    /**
     * Create a fresh API context per scenario/test
     */
    public void createContext(String baseUrl, PwSession session) throws JsonProcessingException {
        this.request = session.request();
        APIRequest.NewContextOptions opts = new APIRequest.NewContextOptions()
                .setBaseURL(baseUrl);
        this.api = request.newContext(opts);
        this.baseUrl = baseUrl;
        JsonNode jsonData = generateToken("santosht1@gmail.com", "Test1234");
        tokenID = jsonData.get("token").asText();
        userID = jsonData.get("userId").asText();
    }

    /***
     * Method create a tokenID
     */
    public JsonNode generateToken(String uname, String pword) throws JsonProcessingException {
        LoginReq loginReq = new LoginReq();
        loginReq.setUserEmail(uname);
        loginReq.setUserPassword(pword);
        String path = "/api/ecom/auth/login";
        String url = buildUrl(path);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        String body = Json.toJson(loginReq);
        RequestOptions options = RequestOptions.create()
                .setData(body == null ? "" : body);
        if (headers != null && !headers.isEmpty()) {//headers
            headers.forEach(options::setHeader);
        }
        APIResponse response = api.post(url, options);
        JsonNode json = mapper.readTree(response.text());
        return json;
    }

    /**
     * Removes any Content-Type key (case-insensitive).
     *
     * @return
     */
    public Map<String, String> configureToken() {
        Map<String, String> _ldefaultHeaders = new HashMap<>();
        _ldefaultHeaders.put("authorization", tokenID);
        return _ldefaultHeaders;
    }

    /**
     * Default JSON headers + optional bearer; used for JSON calls only.
     */
    public Map<String, String> defaultJsonHeaders() {
        Map<String, String> _ldefaultHeaders = new HashMap<>();//
        _ldefaultHeaders.put("authorization", tokenID);
        _ldefaultHeaders.put("Content-Type", "application/json");
        return _ldefaultHeaders;
    }

    /**
     * POST multipart/form-data.
     *
     * @param path      relative path (e.g., "api/ecom/product/add-product")
     * @param multipart key-value map for multipart fields:
     *                  - String values are sent as text fields
     *                  - java.nio.file.Path values are sent as files
     *                  - byte[] values are sent as files (with default name)
     *                  - FilePayload for fine-grained file control (name, mimeType, buffer)
     * @return APIResponse
     */
    public APIResponse postMultipart(String path, Map<String, Object> multipart) {
        String url = buildUrl(path);

        // Defensive copy & normalize: convert non-supported types to String
        FormData form = FormData.create();
        if (multipart != null) {
            multipart.forEach((k, v) -> addPart(form, k, v));
        }

        RequestOptions options = RequestOptions.create()
                // DO NOT set "Content-Type" manually for multipart; Playwright sets boundary automatically.
                .setMultipart(form);

        Map<String, String> _lheaders = configureToken();
        if (_lheaders != null && !_lheaders.isEmpty()) {//headers
            _lheaders.forEach(options::setHeader);
        }

        APIResponse response = api.post(url, options);
        ensureSuccess(response, "POST", url, "<multipart form>");
        return response;
    }

    /**
     * POST JSON with a raw JSON string body.
     *
     * @param path relative path, e.g., "api/ecom/auth/login"
     * @param body raw JSON string (e.g., "{\"userEmail\":\"...\",\"userPassword\":\"...\"}")
     * @return APIResponse from Playwright
     */
    public APIResponse postJson(String path, String body) {
        String url = buildUrl(path);

        RequestOptions options = RequestOptions.create()
                .setData(body == null ? "" : body);
        Map<String, String> _lheaders = defaultJsonHeaders();
        if (_lheaders != null && !_lheaders.isEmpty()) {//headers
            _lheaders.forEach(options::setHeader);
        }
        APIResponse response = api.post(url, options);
        ensureSuccess(response, "POST", url, body);
        return response;
    }

    private void addPart(FormData form, String name, Object value) {
        if (value == null) {
            form.set(name, "");
            return;
        }
        if (value instanceof String) {
            form.set(name, (String) value);
            return;
        }
        if (value instanceof Integer) {
            form.set(name, (Integer) value);
            return;
        }
        if (value instanceof Boolean) {
            form.set(name, (Boolean) value);
            return;
        }
        if (value instanceof Path) {
            form.set(name, (Path) value);
            return;
        }          // file path
        if (value instanceof byte[]) {
            form.set(name, new FilePayload(name, null, (byte[]) value));
            return;
        }
        if (value instanceof FilePayload) {
            form.set(name, (FilePayload) value);
            return;
        }

        // Fallback: stringify other numeric types (long, double, BigDecimal, etc.)
        form.set(name, String.valueOf(value));
    }

    /**
     * Helper to build a FilePayload for multipart file parts.
     */
    public static FilePayload filePayload(String filePath, String contentType) {
        try {
            byte[] bytes = Files.readAllBytes(Path.of(filePath));
            String fileName = Path.of(filePath).getFileName().toString();
            return new FilePayload(fileName, contentType, bytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + filePath, e);
        }
    }

    /**
     * Close per-scenario context
     */
    public void closeContext() {
        if (api != null) {
            api.dispose();
            api = null;
        }
    }

    /**
     * POST JSON with a raw JSON string body.
     *
     * @param path relative path, e.g., "api/ecom/auth/login"
     * @param body raw JSON string (e.g., "{\"userEmail\":\"...\",\"userPassword\":\"...\"}")
     * @return APIResponse from Playwright
     */
    public APIResponse deleteJson(String path, String body) {
        String url = buildUrl(path);

        RequestOptions options = RequestOptions.create()
                .setData(body == null ? "" : body);
        Map<String, String> _lheaders = defaultJsonHeaders();
        if (_lheaders != null && !_lheaders.isEmpty()) {
            _lheaders.forEach(options::setHeader);
        }
        APIResponse response = api.delete(url, options);
        ensureSuccess(response, "POST", url, body);
        return response;
    }

    // Requires a serializer (e.g., Jackson) if you plan to use it.

    public APIResponse postJson(String path, Object payload) {
        String json = Json.toJson(payload); // your Jackson helper
        return postJson(path, json);
    }

    //=================================================
    // -------------------- Helpers --------------------
    private String buildUrl(String path) {
        String p = path == null ? "" : path.trim();
        p = p.startsWith("/") ? p.substring(1) : p;
        return this.baseUrl + "/" + p;
    }

    private void ensureSuccess(APIResponse res, String method, String url, String body) {
        if (res == null) {
            throw new RuntimeException(method + " " + url + " failed: response is null");
        }
        int status = res.status();
        if (status < 200 || status >= 300) {
            String text;
            try {
                text = res.text();
            } catch (RuntimeException e) {
                // Fallback if body can't be read
                byte[] buf = res.body();
                text = buf == null ? "<no body>" : new String(buf, StandardCharsets.UTF_8);
            }
            String safeBody = body == null ? "<no request body>" : body;
            throw new RuntimeException(
                    String.format("%s %s failed: %d %s%nRequestBody: %s%nResponseBody: %s",
                            method, url, status, res.statusText(), safeBody, text));
        }
    }

    //===================================================
    // ------------ Convenience HTTP methods ------------
    public APIRequestContext getContext() {
        return api;
    }

    public APIResponseWrapper get(String path) {
        return new APIResponseWrapper(api.get(path));
    }

    public APIResponseWrapper get(String path, Map<String, String> queryParams) {
        RequestOptions ro = RequestOptions.create();
        if (queryParams != null) {
            queryParams.forEach(ro::setQueryParam);
        }
        return new APIResponseWrapper(api.get(path, ro));
    }

    public APIResponseWrapper post(String path, String jsonBody) {
        RequestOptions ro = RequestOptions.create().setData(jsonBody);
        return new APIResponseWrapper(api.post(path, ro));
    }

    public APIResponseWrapper put(String path, String jsonBody) {
        RequestOptions ro = RequestOptions.create().setData(jsonBody);
        return new APIResponseWrapper(api.put(path, ro));
    }

    public APIResponseWrapper patch(String path, String jsonBody) {
        RequestOptions ro = RequestOptions.create().setData(jsonBody);
        return new APIResponseWrapper(api.patch(path, ro));
    }

    public APIResponseWrapper delete(String path) {
        return new APIResponseWrapper(api.delete(path));
    }


}
