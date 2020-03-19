package stepdefs;

import com.google.inject.Inject;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.HomePage;


@ScenarioScoped
public class HomePageSteps {
    @Inject
    HomePage homePage;

    private Logger logger = LoggerFactory.getLogger(HomePageSteps.class);


    @Then("^user opened registration page$")
    public void userIsOpeningRegistrationPage() {
        homePage.openRegistrationPage();
        logger.info("Home page is open");

    }
}