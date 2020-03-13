package stepdefs;

import pages.HomePage;
import com.codeborne.selenide.Selenide;
import com.google.inject.Inject;
import cucumber.api.java.en.Given;
import utils.TestConfig;

public class NavigationStep {
    @Inject
    BrowserSteps browserSteps;

    @Given("^user is on home page$")
    public void userIsOnHomePage() {
        browserSteps.startBrowserIfNeeded();
        Selenide.open(TestConfig.DEV_URL);
        new HomePage().load();
    }
}
