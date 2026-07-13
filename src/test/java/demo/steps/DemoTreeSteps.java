package demo.steps;

import com.microsoft.playwright.Page;
import demo.hooks.PlaywrightHooks;
import demo.pages.ExceptionPage;
import demo.pages.HomePage;
import demo.pages.LoginPage;
import demo.playwright.PwSession;
import demo.support.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.simple.parser.ParseException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DemoTreeSteps {

    private final Page page;
    private final LoginPage loginPage;
    private final HomePage homePage;
    private final ExceptionPage exceptionPage;

    public DemoTreeSteps(PwSession session) {
        this.page = PlaywrightHooks.page;
        this.loginPage = new LoginPage(page);
        this.homePage  = new HomePage(page);
        this.exceptionPage = new ExceptionPage(page);

    }

    @Given("user opens the login page Demo 3")
    public void user_opens_login_page() {
        loginPage.open();
    }

    @When("user logs in with username {string} and password {string} Demo 3")
    public void user_logs_in(String user, String pass) throws IOException, ParseException {
        loginPage.enterUsername(user).enterPassword(pass).submit();

    }

    @Then("home page is displayed Demo 3")
    public void home_page_is_displayed() throws IOException {
        assertTrue(homePage.isLoaded(), "Home page should be visible after login");
    }

    @And("user log out from Application Demo 3")
    public void user_log_out() throws IOException
    {
        homePage.click_Logout();
    }

    @And("user click on {string} and {string} check if second {string} is visible")
    public void userClicksAddAndCheckRow2Visible(String editBtn, String addBtn, String rowLocator) {
        exceptionPage.checkNewRow(editBtn, addBtn, rowLocator);
    }

    @And("check {string} count is {int}")
    public void checkLocatorCountIs(String key, int expected)
    {
        assertEquals(expected, exceptionPage.veriyAllRowCount(key), "Count of Elements are matching");
    }

}
