package demo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import config.Config;
import demo.locatorOperation.LocatorReader;
import java.util.List;


public class BasePage {
    protected final Page page;
    private final String pageName;

    protected BasePage(Page page, String pageName) {
        this.page = page;
        this.pageName = pageName;
    }

    /**
     *
     * @param key - key paramter from the json file
     * @return - locator depending on the key passed
     */
    public Locator getStableLocator(String key) {
        LocatorReader locatorReader = new LocatorReader(pageName);
        String rawValue = locatorReader.getLocatorValue(key);
        String value = rawValue == null ? "" : rawValue.trim();
        Locator _locator = page.locator(value);
        return stabilize(_locator, Config.get().defaultTimeoutMs());
    }

    /**
     * Applies a consistent stabilization routine:
     * 1) wait for ATTACHED (DOM attached),
     * 2) scroll into view,
     * 3) wait for default visible/enabled (Playwright’s default waitFor()).
     */
    private static Locator stabilize(Locator loc, long timeoutMs) {
        //Wait for state to be attached
        loc.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.ATTACHED)
                .setTimeout(timeoutMs));

        // Scroll may throw if element is detached; a short retry is handled by the next wait
        loc.scrollIntoViewIfNeeded(new Locator.ScrollIntoViewIfNeededOptions()
                .setTimeout(timeoutMs));

        // Default wait: ensures it's actionable (visible, stable) before returning
        loc.waitFor(new Locator.WaitForOptions().setTimeout(timeoutMs));
        return loc;
    }

    /**
     *
     * @param key - key paramter from the json file
     * @return - list of locators depending on the key passed
     */
    public List<Locator> getAllStableLocators(String key) {
        // Reuse your reader, but avoid re-reading per call if possible (optional)
        LocatorReader locatorReader = new LocatorReader(pageName);
        String rawValue = locatorReader.getLocatorValue(key);
        String value = rawValue.trim();

         Locator base = page.locator(value);
        // Wait for the first match to be visible and stable, then scroll it into view.
        Locator first = base.first();
        try {
            first.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.ATTACHED)
                    .setTimeout(Config.get().defaultTimeoutMs()));
            first.scrollIntoViewIfNeeded();
            first.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(Config.get().defaultTimeoutMs()));
        } catch (RuntimeException e) {
            // Provide a helpful message for debugging flaky waits
            throw new IllegalStateException(
                    "Timed out waiting for locator " + value +
                            "' to become VISIBLE/STABLE within " + (long) Config.get().defaultTimeoutMs() + " ms", e);
        }
        // Return all located elements after the first one is confirmed usable
        return base.all();
    }

}
