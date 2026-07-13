package demo.pages;

import com.microsoft.playwright.Page;
import demo.support.TestContext;

import java.io.IOException;

public class PracticePage extends BasePage{

    public PracticePage(Page page)
    {
        super(page, "home");
    }
    public void click_Exception() throws IOException {
        getStableLocator("link_exception").click();
    }
}
