package demo.hooks;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.microsoft.playwright.*;
import config.Config;
import demo.api.ApiClient;
import io.cucumber.java.*;
import demo.playwright.PwSession;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.apache.commons.io.file.PathUtils.deleteDirectory;

public class PlaywrightHooks {

    private final PwSession session;
    public static ApiClient apiClient;
    public static Page page;

    /**
     * Executes before every scenario.
     * Responsibilities:
     * - Initialize Playwright session (browser, context, page)
     * - Read configuration from : Maven command > Env variables > config.properties > defaults
     */
    public PlaywrightHooks(PwSession session) {
        this.session = session;
    }

    /**
     * Executes before every scenario.
     * Responsibilities:
     * - Delete the older Extent report folder before starting execution
     */
    @Before("not @api")
    public void setup(Scenario scn) throws Exception {
        page = session.ctx.newPage(); // Open browsers
        page.setDefaultTimeout(Config.get().defaultTimeoutMs());
        page.navigate(Config.get().baseUrl());
        scn.log("🟢 Scenario started : " + scn.getName());
    }

    @BeforeAll
    public static void beforeAllTests() {
        Path allureFolder = Paths.get(System.getProperty("user.dir") + "\\allure-results");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(allureFolder)) {
            for (Path p : stream) {
                if (Files.isRegularFile(p)) {
                    Files.deleteIfExists(p);  // use method from Option 1
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
        Path extentFolder = Paths.get(System.getProperty("user.dir") + "\\reports");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(extentFolder)) {
            for (Path p : stream) {
                if (Files.isDirectory(p)) {
                    deleteDirectory(p);   // use method from Option 1
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Before("@api")
    public void beforeApiScenario() throws JsonProcessingException {
        apiClient = new ApiClient();
        // default headers if needed
        apiClient.createContext(Config.get().apiBaseUrl(), session);
    }

    @After("@api")
    public void afterApiScenario() {
        if (apiClient != null) {
            apiClient.closeContext();
        }
    }

    /**
     * Utility for StepDefs to access the client
     */
    public static ApiClient api() {
        return apiClient;
    }

    /**
     * Executes after every step.
     * Responsibilities:
     * - Log step status (Passed / Failed) into Extent report
     * - On failure, capture screenshot and attach to:
     * ✔ Cucumber report
     * ✔ Extent report (thread-safe)
     */
    @AfterStep("not @api")
    public void afterStep(Scenario scenario) {
        try {
            if (scenario.isFailed()) {
                byte[] png = page.screenshot(new com.microsoft.playwright.Page.ScreenshotOptions().setFullPage(true));
                scenario.attach(png, "image/png", "Failed screenshot");
  /*              byte[] screenshot = page.screenshot();
                Allure.getLifecycle().addAttachment( "Step Screenshot", "image/png", "png", screenshot);
                Allure.addAttachment("Step Screenshot", "image/png",
                        new java.io.ByteArrayInputStream(screenshot), "png");*/

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @After
    public void attachScreenshot(Scenario scenario)
    {
        if (scenario.isFailed()) {
            byte[] screenshot = page.screenshot(
                    new Page.ScreenshotOptions().setFullPage(true)
            );
            scenario.attach(screenshot, "image/png", "Failed Screenshot");
        }
    }


    /**
     * Executes after every scenario.
     * Responsibilities:
     * - Close Playwright page, context, and browser
     */
    @After("not @api")
    public void teardown(Scenario scenario) {

        try {
            session.ctx.close();
        } catch (Exception e) {
            scenario.log("[PwSession] context.close() failed: " + e.getMessage());
        }

        try {
            session.browser.close();
        } catch (Exception e) {
            scenario.log("[PwSession] browser.close() failed: " + e.getMessage());
        }
        try {
            session.pw.close();
        } catch (Exception e) {
            scenario.log("[PwSession] playwright.close() failed: " + e.getMessage());
        }
        if (apiClient != null) {
            apiClient.closeContext();
        }

    }
}
