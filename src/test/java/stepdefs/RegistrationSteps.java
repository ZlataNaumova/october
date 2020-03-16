package stepdefs;

import com.codeborne.selenide.Condition;
import com.google.inject.Inject;
import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.RegistrationPage;
import pages.SuccessRegistrationPage;

import static utils.RandomUtils.generateRandomEmail;

@ScenarioScoped
public class RegistrationSteps {
    @Inject
    SuccessRegistrationPage successRegistrationPage;
    @Inject
    RegistrationPage registrationPage;

    public User createdUser;

    private Logger logger = LoggerFactory.getLogger(HomePageSteps.class);

    @When("^User submitted registration form with data$")
    public void userFilledAllFields(DataTable table) {
        User user = table.asList(User.class).get(0);
        registerUser(user);
    }


    @Then("^user will see success message which contains correct email$")
    public void userWillSeeSuccessMessageWhichContainsCorrectEmail() {
        successRegistrationPage.emailText(createdUser.getEmail()).shouldBe(Condition.visible);
        logger.info("Users Email appeared on the page");
    }

    @When("^User submitted registration form with data: \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"$")
    public void userFillFields(String firstName, String lastName, String password) {
        User user = User.builder().lastName(lastName)
                .firstName(firstName)
                // in order to make test repeatable, email has to be unique
                .email(generateRandomEmail())
                .password(password).build();
        registerUser(user);
        // At this point user is created, assign user to global variable for further assertions.
        createdUser = user;
    }

    private void registerUser(User user) {
        registrationPage.setUserData(user)
                .switchHasNoPartner()
                .acceptTermsAndCondition()
                .submitRegistrationForm();
    }
}
