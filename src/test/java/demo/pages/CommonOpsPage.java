package demo.pages;

import com.microsoft.playwright.Page;
import demo.support.TestContext;

import java.io.IOException;

public class CommonOpsPage extends  BasePage {

    public CommonOpsPage(Page page) {
        super(page,"commonPage");

    }

    public void click_Practice() throws IOException {
        getStableLocator("link_practice").click();
    }


}
