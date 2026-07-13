package demo.steps;

import com.microsoft.playwright.*;
import demo.hooks.PlaywrightHooks;
import demo.pages.*;
import demo.support.TestContext;
import demo.playwright.PwSession;
import io.cucumber.java.en.*;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginSteps {

    private final LoginPage loginPage;
    private final Page page;
    private final HomePage homePage;
    private final PracticePage practicePage;
    private final ExceptionPage exceptionPage;
    private final CommonOpsPage commonOpsPage;

    public LoginSteps(PwSession session) {
        this.page = PlaywrightHooks.page;
        this.loginPage = new LoginPage(page);
        this.homePage  = new HomePage(page);
        this.practicePage =new PracticePage(page);
        this.exceptionPage = new ExceptionPage(page);
        this.commonOpsPage = new CommonOpsPage(page);
    }

    @Given("user opens the login page")
    public void user_opens_login_page() {
        loginPage.open();
    }

    @When("user logs in with username {string} and password {string}")
    public void user_logs_in(String user, String pass) throws IOException, ParseException {
        loginPage.enterUsername(user).enterPassword(pass).submit();

    }

    @Then("home page is displayed")
    public void home_page_is_displayed() throws IOException {
        assertTrue(homePage.isLoaded(), "Home page should be visible after login");
    }

    @And("user log out from Application")
    public void user_log_out() throws IOException
    {
        homePage.click_Logout();
    }


    @And("user clicks on Practice link and then Test Exceptions")
    public void userClicksOnPracticeThenTestExceptions() throws IOException {
        commonOpsPage.click_Practice();
        practicePage.click_Exception();

    }

    @Then("Test Exception page should open")
    public void testExceptionPageShouldOpen() throws IOException {
        assertTrue(exceptionPage.isLoaded(),"Exception page is displaying");
    }

    @And("check {string} is visible")
    public void checkIsVisible(String locatorValue)
    {
        assertTrue(exceptionPage.isTC5visible(locatorValue), "TC 5 is visible");
    }

    @And("edit value in the {string} textbox")
    public void editValueIntheTextbox(String locatorValue) {
        assertEquals("Pizza", exceptionPage.editTextboxOperation(locatorValue, "btn_edit", "pizza"));
    }


}
