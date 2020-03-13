package stepdefs;

import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.$;


@ScenarioScoped
public class HomePageSteps {


    private Logger logger = LoggerFactory.getLogger(HomePageSteps.class);


    @Then("^user opened registration page$")
    public void userIsOpeningRegistrationPage() {
        $(By.id("button-sign-up")).click();

    }
}