package stepdefs;

import com.codeborne.selenide.Condition;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import models.User;
import org.openqa.selenium.By;
import pages.RegistrationPage;

import static com.codeborne.selenide.Selenide.$;

public class RegistrationSteps {
    @Given("^user is on registration page$")
    public void userIsOnRegistrationPage() {
        new RegistrationPage().load();
    }


    @When("^user filled all fields$")
    public void userFilledAllFields() {
        User user = new User("sdf@sdfqq.sd", "test", "test", "1qaz!QAZ");

        RegistrationPage registrationPage = new RegistrationPage();
        registrationPage.setUserData(user);
        registrationPage.switchHasNotPartner.click();
        registrationPage.checkboxAreement.click();

    }

    @And("^submit form$")
    public void submitForm() {
        new RegistrationPage().submitBtn.click();
    }

    @Then("^user will see success message which contains correct email$")
    public void userWillSeeSuccessMessageWhichContainsCorrectEmail() {
        $(By.className("success")).waitUntil(Condition.appear, 310);
        $(By.xpath("//font[contains(text(),'sdf@.sdf')]")).shouldHave(Condition.visible);
    }
}
