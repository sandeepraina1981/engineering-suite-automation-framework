package demo.support;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class TestContext
{
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;
    private String baseUrl;

    public Playwright getPlaywright() {
        return playwright;
    }
    public Browser getBrowser() { return browser; }
    public BrowserContext getContext() { return context; }
    public Page getPage() { return page; }
    public String getBaseUrl() { return baseUrl; }

    public void setPlaywright(Playwright p) { this.playwright = p; }
    public void setBrowser(Browser b) { this.browser = b; }
    public void setContext(BrowserContext c) { this.context = c; }
    public void setPage(Page p) { this.page = p; }
    public void setBaseUrl(String url) { this.baseUrl = url; }

}
