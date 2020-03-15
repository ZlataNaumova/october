package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static utils.TestConfig.DEFAULT_TIMEOUT_MS;

public class SuccessRegistrationPage extends BasePage{

    public SelenideElement emailText(String email){
        return $(By.xpath("//form[contains(.,'"+email+"')]"));
    }

    public SelenideElement submitBtn(){ return $(By.id("gotit")); }

    @Override
    public SuccessRegistrationPage load() {
        submitBtn().waitUntil(Condition.enabled, DEFAULT_TIMEOUT_MS);
        return this;
    }
}
