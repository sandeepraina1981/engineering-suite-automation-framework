package demo.pages;

import com.microsoft.playwright.Page;
import demo.support.TestContext;
import org.json.simple.parser.ParseException;


import java.io.IOException;

public class LoginPage extends BasePage{

    public LoginPage(Page page)
    {
        super(page, "login");
    }

    public LoginPage open() {
        return this;
    }

    public LoginPage enterUsername(String user) throws IOException, ParseException {
        getStableLocator("uname").fill(user);
        return this;
    }

    public LoginPage enterPassword(String pass) throws IOException {
        getStableLocator("pword").fill(pass);
        return this;
    }

    public void submit() throws IOException {
        getStableLocator("login").click();
    }
}
