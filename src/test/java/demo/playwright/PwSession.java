package demo.playwright;

import com.microsoft.playwright.*;
import config.Config;

import java.util.Arrays;


public class PwSession implements AutoCloseable {


    public final Playwright pw;
    public final Browser browser;
    public final BrowserContext ctx;

    /**
     * Responsibilities:
     * Initialize Playwright session (browser, context, page, APIRequest, APIRequestContext)
     */
    public PwSession() {
        Config cfg = Config.get();
        this.pw = Playwright.create();
        BrowserType type = switch (cfg.browser().toLowerCase()) {
            case "firefox" -> pw.firefox();
            case "webkit" -> pw.webkit();
            default -> pw.chromium();
        };
        boolean parHead = cfg.headless();
        BrowserType.LaunchOptions launch = new BrowserType.LaunchOptions()
                .setHeadless(parHead).setArgs(Arrays.asList(new String[]{"--start-maximized"}));
        if (cfg.slowMoMs() > 0) launch.setSlowMo(cfg.slowMoMs());
        this.browser = type.launch(launch);

        Browser.NewContextOptions ctxOpts = new Browser.NewContextOptions().setViewportSize(null);
        this.ctx = browser.newContext(ctxOpts);
        // Trace policy
        switch (cfg.trace()) {
            case "on" -> ctx.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true));
            case "on-first-retry" -> { /* enable in retry setup if you add retries */ }
            case "retain-on-failure" -> { /* start/stop on failure in hooks */ }
            default -> { /* off */ }
        }
    }


    public APIRequest request() {
        return pw.request();
    }

    @Override
    public void close() {
        try {
            ctx.close();
        } finally {
            try {
                browser.close();
            } finally {
                pw.close();
            }
        }
    }
}
