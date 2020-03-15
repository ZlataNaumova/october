package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import models.User;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static utils.TestConfig.LONG_TIMEOUT_MS;

public class RegistrationPage extends BasePage {

    public SelenideElement switchHasNotPartner() {return $(By.xpath("//label[@for='no']"));}

    public SelenideElement checkboxAgreement() {
        return $(By.className("agreements"));
    }

    public SelenideElement submitBtn() {
        return $(By.xpath("//button[@type='submit' and @class='large purple']"));
    }

    public SelenideElement firstNameField() {
        return $(By.id("firstname"));
    }
    public SelenideElement lastNameField() {return $(By.id("lastname"));}
    public SelenideElement emailField() {return $(By.id("email"));}
    public SelenideElement passwordField() {return $(By.id("password"));}

    @Override
    public RegistrationPage load() {
        return this;
    }

    public RegistrationPage setUserData(User user) {
        firstNameField().setValue(user.getFirstName());
        lastNameField().setValue(user.getLastName());
        emailField().setValue(user.getEmail());
        passwordField().setValue(user.getPassword());
        return this;
    }

    public RegistrationPage switchHasNoPartner() {
        switchHasNotPartner().click();
        return this;
    }

    public RegistrationPage acceptTermsAndCondition(){
        checkboxAgreement().click();
        return this;
    }

    public SuccessRegistrationPage submitRegistrationForm() {
        submitBtn().waitUntil(Condition.enabled, LONG_TIMEOUT_MS).doubleClick();
        return new SuccessRegistrationPage().load();
    }


}
