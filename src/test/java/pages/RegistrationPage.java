package pages;

import com.codeborne.selenide.SelenideElement;
import models.User;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class RegistrationPage extends BasePage {

    public SelenideElement switchHasNotPartner = $(By.xpath("//label[@for='no']"));
    public SelenideElement checkboxAreement = $(By.className("agreements"));
    public SelenideElement submitBtn = $(By.xpath(".//button[@type='submit']"));

    @Override
    public RegistrationPage load() {
        return this;
    }

    public RegistrationPage setUserData(User user){
        $(By.id("firstname")).setValue(user.getFirstName());
        $(By.id("lastname")).setValue(user.getLastName());
        $(By.id("email")).setValue(user.getEmail());
        $(By.id("password")).setValue(user.getPassword());
        return this;
    }


}
