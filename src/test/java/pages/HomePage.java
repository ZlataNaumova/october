package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class HomePage extends BasePage {


    public SelenideElement singUpBtn() {
        return $(By.id("button-sign-up"));
    }

    public void openRegistrationPage() {
        singUpBtn().click();
    }

    @Override
    public HomePage load() {
        $(By.id("logo")).shouldBe(Condition.visible);
        return this;
    }


}
