package stepdefs;

import com.codeborne.selenide.Condition;
import com.google.inject.Inject;
import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.RegistrationPage;
import pages.SuccessRegistrationPage;

public class RegistrationSteps {
    @Inject
    SuccessRegistrationPage successRegistrationPage;
    @Inject
    RegistrationPage registrationPage;

    private Logger logger = LoggerFactory.getLogger(HomePageSteps.class);

    @When("^User submitted registration form with data$")
    public void userFilledAllFields(DataTable table) {
        User user = table.asList(User.class).get(0);
        registerUser(user);

    }


    @Then("^user will see success message which contains correct email \"([^\"]*)\"$")
    public void userWillSeeSuccessMessageWhichContainsCorrectEmail(String email) {
        successRegistrationPage.emailText(email).shouldHave(Condition.visible);
        logger.info("Users Email appeared on the page");

    }

    @When("^User submitted registration form with data: \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\",\"([^\"]*)\"$")
    public void userFillFields(String firstName, String lastName, String email, String password) {
        User user = User.builder().lastName(lastName).firstName(firstName).email(email).password(password).build();
        registerUser(user);
    }

    private void registerUser(User user) {
        registrationPage.setUserData(user)
                .switchHasNoPartner()
                .acceptTermsAndCondition()
                .submitRegistrationForm();
    }
}
