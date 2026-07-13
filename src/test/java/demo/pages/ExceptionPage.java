package demo.pages;

import com.microsoft.playwright.Page;
import demo.support.TestContext;

import java.io.IOException;

public class ExceptionPage extends  BasePage{

    public ExceptionPage(Page page) {
        super(page,"home");

    }
    public boolean isLoaded() throws IOException {
        return getStableLocator("txtException").isVisible();
    }

    public boolean isTC5visible(String locatorValue)
    {
        return getStableLocator(locatorValue).isVisible();
    }

    public String editTextboxOperation(String locatorValue, String btnValue, String newValue) {
        getStableLocator(btnValue).click();
        getStableLocator(locatorValue).clear();
        getStableLocator(locatorValue).fill(newValue);
        return getStableLocator(locatorValue).getAttribute("value");

    }

    public boolean checkNewRow(String editbtn, String addbtn, String rowLocator)
    {
        getStableLocator(editbtn).click();
        getStableLocator(addbtn).click();
        return getStableLocator(rowLocator).isVisible();
    }
    public int veriyAllRowCount(String rowLocator)
    {
        return getAllStableLocators(rowLocator).size();
    }
}
