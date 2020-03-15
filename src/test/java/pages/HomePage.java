package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.$;

public class HomePage extends BasePage {

    private Logger logger = LoggerFactory.getLogger(HomePage.class);

    public SelenideElement singUpBtn(){
        return $(By.id("button-sign-up"));
    }

    public void openRegistrationPage(){
        singUpBtn().click();
    }

    @Override
    public HomePage load() {
        $(By.id("logo")).shouldBe(Condition.visible);
        return this;
    }





}
