package demo.pages;

import com.microsoft.playwright.Page;
import demo.support.TestContext;

import java.io.IOException;

public class HomePage extends BasePage {

      public HomePage(Page page)
      {
          super(page, "home");
      }

    public boolean isLoaded() throws IOException {
        return getStableLocator("txtmsg").isVisible();
    }

    public void click_Logout() throws IOException {
        getStableLocator("link_Logout").click();
    }
}
